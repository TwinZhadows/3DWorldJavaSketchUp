class Model
  
  @obj 
  attr_accessor :position
  attr_accessor :animation
  attr_accessor :dimen
  attr_accessor :angle
  attr_accessor :collision
  attr_accessor :isStop
  attr_accessor :startTime
  attr_accessor :stopTime
  attr_accessor :isAnimated 
  attr_accessor :bound
  attr_accessor :stepCount
  attr_accessor :currentAnimationTime
  def add_model( file,scale,rotation, x, y, z)
    
    
    model = Sketchup.active_model
    
    drone_path = Sketchup.find_support_file file, "Components/Objects/"
    
    man = model.definitions.load drone_path
    tr = Geom::Transformation.translation [0, 0, 0]
    tr = Geom::Transformation.translation [x, y, z]
    sc = Geom::Transformation.scaling scale, scale, scale #0.3
    r = Geom::Transformation.rotation [x,y,z],[0,0,1],rotation.degrees       
    @angle = rotation.degrees
    @obj = Sketchup.active_model.entities.add_instance(man,tr*sc )
    box = @obj.bounds
    @dimen = Geom::Point3d.new(box.width.to_i, box.height.to_i,0)
    @obj.transform! r
    c0 = box.corner(0).transform r
    c1 = box.corner(1).transform r
    c2 = box.corner(2).transform r
    c3 = box.corner(3).transform r

    @bound = [c0, c1, c2, c3]
    
    puts "#{box.width.to_i}, #{box.height.to_i}\n"
    @position = Geom::Point3d.new(x, y, z) 
    @isStop = false
    @stepCount = 1

  end
  
  def setAnimation(steps)
    @animation = Array.new
    @animation = steps
    @collision = false
    @stop = false
    @isAnimated = false
    puts @animation
     puts "length = #{@animation.length}"
  end


  def resetStepCount()
    @stepCount = 0
  end
  def increaseStepCount()
    @stepCount += 1
    if @stepCount == 10
        @currentAnimationTime += 1
        @stepCount = 1
    end
  end

  def getCurrentAnimationTime()
    @currentAnimationTime
  end

  def setCollision(val)
    @collision = val
  end
  
  def getCollision()
    @collision
  end

  def setStopTime(time)
    @stopTime = time
  end

  def self.animated(val)
    @isAnimated = val
  end

  def isAnimated()
    @isAnimated
  end

  def getStopTime()
    @stopTime
  end

  def setStartTime(time)
    @startTime = time
    @currentAnimationTime = time
  end

  def getStartTime()
    @startTime
  end
   
  def getAnimation(index)
     for movStep in @animation
       if movStep.time == index
          return movStep.tran
       end   
     end
     return Geom::Transformation.translation [0,0,0]
  end

  def updateAnimation(steps)
     tmp = @animation
     @collision = false
     @stop = false
     @animation = Array.new
     for step in tmp
       if step.time < @startTime
          @animation.push(step)
          puts step.time
       end
     end
    
    @animation = @animation + steps
    puts @animation
  end
  
  def getDimension()
    @dimen
  end
  
  def getPosition()
    @position
  end
  def stop()
    @isStop = true
  end

  def isStop()
    @isStop
  end

  def setAngle(val)
    @angle = val
  end

  def getAngle()
    @angle*180/(Math::PI)
  end

  def getBound()
    @bound
  end

  
 def animate(index)
    
    interval = 0.1
    clock = 0
    limit = 1
    
	 # t0 = Time.now
	 # while (Time.now - t0 <0.1)
	 #	wait	
	 # end
          0.upto(@animation.length-1){|i|
                movStep = @animation[i]
                if i>0
                  prevStep = @animation[i-1]
                end
                
                if movStep.time == index 
                    tran = movStep.tran
                    @obj.transform! tran
                    @bound[0].transform! tran
                    @bound[1].transform! tran
                    @bound[2].transform! tran
                    @bound[3].transform! tran
                    anim = tran.to_a
                    x = anim[12]
                    y = anim[13]
                    pi = Math::PI
                    rotation = Math.atan2(anim[1],anim[0])#*180/pi
                    @angle += rotation
                    @position.transform! tran
                    puts "index = #{index}, step at #{movStep.time}"
                    break
                els if (index%1 ==0.5) && (i>0)&&(index - 0.5 == prevStep.time)
                    tran = prevStep.tran
                    @obj.transform! tran
                    @bound[0].transform! tran
                    @bound[1].transform! tran
                    @bound[2].transform! tran
                    @bound[3].transform! tran
                    anim = tran.to_a
                    x = anim[12]
                    y = anim[13]
                    pi = Math::PI
                    rotation = Math.atan2(anim[1],anim[0])#*180/pi
                    @angle += rotation
                    @position.transform! tran
                    puts "index = #{index}, previous step at #{prevStep.time}"
                    break
                end
 
                }
          end

  
end