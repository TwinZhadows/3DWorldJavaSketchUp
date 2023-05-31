class Animation
  $camera = Camera.new
  $colCount = 0
  $count = 0
  $movModel = []
  $max = 0
  $collPair = []
  attr_accessor :directory
  $isStarted = false
  $staticModel = []
  def setPath(path)
    @directory = path
    puts @directory
  end

  def createAnimation(name)
    steps = Array.new()
    limit = 0.5
    interval = 0.1
    lineNum = 1
    start = 0
    stop = 0

    transformss = Struct.new(:time,:angle,:x,:y,:z)
    movStep = Struct.new(:time,:tran)
    points = Array.new()
    sFile = File.open "#{@directory}#{name}Step.txt", "r"
    if File.zero?("#{@directory}#{name}Step.txt")
        return 0
    end
    sLines = sFile.readlines  

    for line in sLines

      sBuff = line.split(",")
      if lineNum == 1
        start = sBuff[0].to_i
      end
      time = sBuff[0].to_f
      if time > $max
         $max = time
      end
      x = sBuff[1].to_i
      y = sBuff[2].to_i
      z = sBuff[3].to_i
      angle = 0
      if !sBuff[4].nil?
        angle = sBuff[4].to_f
      end
      tmp = transformss.new(time,(interval/limit)*angle, x, y, z)
      points.push(tmp)
      
      lineNum += 1
    end
    sFile.close() 
    stop = start 
    x0 = points[0].x
    y0 = points[0].y
    z0 = points[0].z
    angle0 = points[0].angle.degrees
    t0 = points[0].time
    t1 = Geom::Transformation.new
    msgFile = File.open "#{@directory}Message.txt", "w"

    1.upto(points.length-1) {|i| 
    
    t = points[i].time
    if t >t0
      x = points[i].x
      y = points[i].y
      z = points[i].z
      angle = points[i].angle.degrees

      dx = x-x0
      dy = y-y0
      dz = z-z0

      puts "#{dx},#{dy},#{dz}, #{t0}"
        
        rotation =Geom::Transformation.rotation [x,y,z],[0,0,1],angle
        trans = Geom::Transformation.translation [dx*interval/limit,dy*interval/limit,dz*interval/limit]
        mov = movStep.new(t0,(trans*rotation))  
        steps.push(mov)

        x0 = x
        y0 = y
        z0 = z
        angle0 = angle
        
        if t0%1 == 0
          stop += 1
        end
        t0 = t
    else
        
        msgFile.write("Warning: line #{i} in #{name}Step.txt is skipped\n") 
        
        
    end
    
    }
    msgFile.close()

    if stop > $max
       $max = stop
    end

    for obj in $movModel
      
      if obj.name == name
        if obj.model.getCollision  
           puts "----------------------case1------------------------------"
           
           obj.model.setStartTime(start)
           obj.model.updateAnimation(steps)      
           obj.model.setStopTime(stop)
           obj.model.setCollision(false)
           obj.model.resetStepCount()
           
        else
           puts "----------------------case2------------------------------"
           obj.model.setAnimation(steps)
           obj.model.setStartTime(start)
           obj.model.setStopTime(stop)
           obj.model.setCollision(false)

        end
      end
      
      if obj.name == "user"
        $camera.setAnimation(steps)
      end
    end
    
  end 
  
  def animateModels(startTime)
    limit = 1
    time = startTime

    if !$isStarted
        time = 0
        $isStarted = true
    end
    startAnimTime = Time.now
    while time<=($max-1) 
    i = time 
    0.upto(4){|count|
    for obj in $movModel
      #if obj.model.getCurrentAnimationTime <= i
        dimen = obj.model.getDimension()
        width = dimen.x.to_i
        height = dimen.y.to_i
        angle = obj.model.getAngle()   
        tran = obj.model.getAnimation(i)

        x = (tran.to_a[12])#/0.1*limit #interval
        y = (tran.to_a[13])#/0.1*limit #interval
        pos = obj.model.getPosition()      
        puts "#{obj.name},#{pos.x.to_i}, #{pos.y.to_i}, #{angle} time = #{i}"

        if !obj.model.isStop()
          if isCollision((pos.x).to_i, (pos.y).to_i, x*2, y*2, width, height, obj.name, i,angle )

            obj.model.setCollision(true)
            puts "collision"
            model = Sketchup.active_model
            view = model.active_view
            refreshed_view = view.refresh          
            return 0     
     
          else 

            if(i>=startTime)
                 t0 = Time.now
	         while (Time.now - t0 <0.1)
		   #wait	
	         end
            end
            obj.model.animate(i)
            obj.model.increaseStepCount()
            if $camera.isDynamic()
              if obj.name == "user"
                $camera.animate(i)
              end
            end

            if(i>=startTime)
              model = Sketchup.active_model
              view = model.active_view
              refreshed_view = view.refresh
            end

            if(i == obj.model.getStopTime())
                obj.model.stop()
            end
        end  
      end
     #end
    end 
    
    }
    time = time+0.5
    end
    
    puts "end #{(Time.now-startAnimTime)*1000.0}"
   
        file = File.open "#{@directory}finish.txt", 'w'
        file.write("finished") 
        file.close

    
    
  end
  
  def stopModel(name)
     for model in $movModel
        if model.name == name
            model.model.stop(true)
            break
        end
     end
  end

  def add_model( name, file,scale,rotation, x, y, z, movable)
      
      animation = Struct.new(:name,:model)
      model = Model.new()
      model.add_model( file,scale,rotation, x, y, z)
      if movable 
         tmp = animation.new(name, model)
         $movModel.push(tmp)    
      else
         tmp = animation.new(name, model)
         $staticModel.push(tmp)    
      end
  end
   
  def setCam(x0, y0, z0, x1, y1, z1)  
    
      $camera.setCam(x0, y0, z0, x1, y1, z1)
  
  end

  def setDynamicCam(val)  
    
      $camera.setDynamic(val)
  
  end
  
  def isCollision(px, py, dx, dy, width, height, name, time, angle)
    
    vx = 0
    vy = 0
    if dx != 0
      vx = dx/dx.abs
    end
    if dy != 0
      vy = dy/dy.abs
    end
    
    frontX = px + (width/2)*Math.cos(angle/180*Math::PI)
    frontY = py + (width/2)*Math.sin(angle/180*Math::PI)
    shape = Shape.new

    if shape.isCollision(px+dx, py+dy )
      return true
    end
 
    $movModel.each do |a|
      
      if a.name != name
        model = a.model      
        pos = model.getPosition()
        cx = pos.x.to_i
        cy = pos.y.to_i
        dimen = model.getDimension()
        tAngle = model.getAngle()
        w = (dimen.x.to_i)
        h = (dimen.y.to_i)
        bound = model.getBound

        if isInTriangle(frontX+dx, frontY+dy, bound[0], bound[1], bound[2]) || isInTriangle(frontX+dx, frontY+dy, bound[3], bound[1], bound[2])
          
          model.setCollision(true)
          file = File.open "#{@directory}#{name}Collision.txt", 'w'
          file.write("#{name}, #{width}, #{height}, #{px}, #{py}, #{a.name}, #{w}, #{h}, #{cx}, #{cy}, #{vx}, #{vy}, #{(time+1).to_i}, #{angle},  #{tAngle}") 
          file.close
          
          return true
        end
        #end
      end
    end
    
    return false
    
  end

  def isInTriangle(x, y, v1, v2, v3)

        det = (v1[1] - v3[1]) * (v2[0] - v3[0]) + (v2[1] - v3[1]) * (v3[0] - v1[0])
        b1 = ((y - v3[1]) * (v2[0] - v3[0]) + (v2[1] - v3[1]) * (v3[0] - x)) / det
        b2 = ((y - v1[1]) * (v3[0] - v1[0]) + (v3[1] - v1[1]) * (v1[0] - x)) / det
        b3 = 0

        if ((b1 >= 0) && (b2 >= 0) && (b1 + b2 <= 1))      
            return true
        else             
            return false
        end
  end
  
end