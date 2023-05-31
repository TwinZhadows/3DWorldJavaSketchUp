class Camera
     
  $next_tar = Geom::Point3d.new(0,0,0)
  attr_accessor :animation
  attr_accessor :dynamic
 attr_accessor :eye
 attr_accessor :target
  $angle = 0

  
  def setCam(x0, y0, z0, x1, y1, z1)  
    
    view = Sketchup.active_model.active_view
    @eye = [x0, y0, z0]
    @target = [x1, y1, z1]
    up = [0, 0, 1]
    cam = Sketchup::Camera.new eye, target, up
    view.camera = cam
    @dynamic = false
  end
  
  def setDynamic(val)
    @dynamic = val    
  end

  def isDynamic()
    @dynamic    
  end


  def setAnimation(steps)
    @animation = Array.new
    @animation = steps
    
  end
  
  def nextFrame(view)
    
    dx = 0
    dy = 0
    dz = 0
    rotation = 1
    new_tar = view.camera.target
    new_eye = view.camera.eye

       
    if $next_tar.x != new_tar.x
       dx = ($next_tar.x - new_tar.x)/($next_tar.x - new_tar.x).abs
    end
    if $next_tar.y != new_tar.y
       dy = ($next_tar.y - new_tar.y)/($next_tar.y - new_tar.y).abs
    end
    if $next_tar.z != new_tar.z
       dz = ($next_tar.z - new_tar.z)/($next_tar.z - new_tar.z).abs
    end
    
    new_tar.x = new_tar.x.to_i + dx.to_i
    new_tar.y = new_tar.y.to_i + dy.to_i
    new_tar.z = new_tar.z.to_i + dz.to_i
    
    new_eye.x = new_eye.x + dx
    new_eye.y = new_eye.y + dy
    new_eye.z = new_eye.z + dz
    
    if $angle != 0
       r = Geom::Transformation.rotation(new_tar, [0,0,1], 1)
       new_eye.transform!(r)
    end        
    view.camera.set(new_eye, new_tar, [0,0,1])
    view.show_frame(0.1)
    return (new_tar != $next_tar) && (new_eye != $next_eye)
  end
  
  def setNewTarget(x, y, z, ang)
    $next_tar = Geom::Point3d.new(x,y,z)
    $angle = ang
    if $angle != 0
       eye = Sketchup.active_model.active_view.camera.eye
       r = Geom::Transformation.rotation($next_tar, [0,0,1], $angle)      
       $next_eye = eye.transform!(r)
    end
  end
  
  def animate1(index)
    
    interval = 0.1
    clock = 0
    limit = 1

    @tar = Sketchup.active_model.active_view.camera.target
    @eye = Sketchup.active_model.active_view.camera.eye
    
    timer = UI.start_timer(interval, true) { 
      # Increment clock and test if animation complete 
	    clock += interval 
	    # Perform transformation depending on time 
	    case clock 
      when 0..limit
        

        @eye.transform! @animation[index]
	@tar.transform! @animation[index]
        Sketchup.active_model.active_view.camera.set(@eye,@tar,[0,0,1])
      else 
        UI.stop_timer timer 
        
      end
    }
  end

 def animate(index)
    @tar = Sketchup.active_model.active_view.camera.target
    @eye = Sketchup.active_model.active_view.camera.eye
    interval = 0.1
    clock = 0
    limit = 1
    
	  t0 = Time.now
	  while (Time.now - t0 <0.1)
		#wait	
	  end
        
        @eye.transform! @animation[index]
	@tar.transform! @animation[index]
        Sketchup.active_model.active_view.camera.set(@eye,@tar,[0,0,1])

          model = Sketchup.active_model
          view = model.active_view
          refreshed_view = view.refresh
	
	
    
  end

end