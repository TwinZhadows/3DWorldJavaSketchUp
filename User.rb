  class User 
    
  @obj 
  $userModel
  @position
  $cam = Camera.new
  def add_model(name, x, y, z, rotation, scale)
  
    model = Sketchup.active_model

    drone_path = Sketchup.find_support_file name, "Components/Objects/"
 
    man = model.definitions.load drone_path
    tr = Geom::Transformation.translation [x, y, z]
    sc = Geom::Transformation.scaling scale, scale, scale #0.3
    r = Geom::Transformation.rotation [0,0,0],[0,0,1],rotation.degrees

    @obj = Sketchup.active_model.entities.add_instance(man,tr*sc*r )
    box = @obj.bounds
    puts "#{box.width.to_i}, #{box.height.to_i}\n"
    User.setPosition(x, y, z)
  end
  
  def getUsermodel()
      @obj
  end
  def self.setPosition(x, y, z)
    @position = Geom::Point3d.new(x, y, z) 
  end
  
  def self.getPosition()
    @position
     
  end
  
  def animate(x, y, z, rotation)
    
    interval = 0.02
    clock = 0
    limit = 0.1
   
    current = User.getPosition()
    puts current
    dx = x - current.x
    dy = y - current.y
    dz = z - current.z
    

      
    rotation =Geom::Transformation.rotation [x,y,z],[0,0,1],rotation.degrees*interval/limit 
    translation =Geom::Transformation.translation [dx*interval/limit,dy*interval/limit,dz*interval/limit]
    
    timer = UI.start_timer(interval, true) { 
      # Increment clock and test if animation complete 
	    clock += interval 
	    # Perform transformation depending on time 
	    case clock 
      when 0..limit
        @obj.transform! translation*rotation
	        
      else 
        UI.stop_timer timer 
        
      end
    }
    User.setPosition(x, y, z)
    
  end

  end