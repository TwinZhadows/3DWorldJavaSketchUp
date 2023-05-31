class Shape
  $list = Sketchup.active_model.definitions
  $shapes = []
  def createBlock(name, x, y, z, width, length, height)
    
    comp_def = $list.add name
    $shapes.push(name)
    ent = comp_def.entities
    p1 = [-width/2, -length/2, 0]
    p2 = [-width/2, length/2, 0]
    p3 = [width/2, length/2, 0]
    p4 = [width/2, -length/2, 0]
 
    main_face = ent.add_face p1, p2, p3, p4
    main_face.reverse!
    
    #r = Geom::Transformation.rotation [0, 0, 0], [0, 0, 1], rotation.degrees
    tr = Geom::Transformation.translation [x, y, z]
    #sc = Geom::Transformation.new scale
    #ent.transform_entities r*tr*sc, main_face
    main_face.pushpull height, true
    Sketchup.active_model.active_entities.add_instance(comp_def, tr) ; 
  end
 

  def createCylinder(name, x, y, z, radius, height)
    
    comp_def = $list.add name
    $shapes.push(name)
    ent = comp_def.entities

    normal = [0, 0, 1]
    edge = ent.add_circle [x, y, 0], normal, radius
    face = ent.add_face(edge)
    face.reverse!
    #r = Geom::Transformation.rotation [0, 0, 0], [0, 0, 1], rotation.degrees
    tr = Geom::Transformation.translation [0, 0, z]
    #sc = Geom::Transformation.new scale
    #ents.transform_entities r*tr*sc, face
    face.pushpull height, true   
    Sketchup.active_model.active_entities.add_instance(comp_def, tr) ; 

  end
  
  def self.getSize(name)
    $list.each do |a|
      if a.name == name 
        @w = a.instances[0].bounds.width.to_i
        @h = a.instances[0].bounds.depth.to_i
      end
    end
    return [@w, @h]
  end
  
  def getShape()
    $list
  end
 
  def isCollision(x,y)
    
    $list.each do |a| 
      
      if $shapes.include?(a.name)
        cx = a.instances[0].bounds.center.x.to_i
        cy = a.instances[0].bounds.center.y.to_i
        w  = a.instances[0].bounds.width.to_i
        h  = a.instances[0].bounds.depth.to_i
        
        if x>= cx - w/2 && x<= cx + w/2
          if y>= cy - h/2 && y<= cy + h/2
            return true
          end
        end
      end
    end
    return false
  end
  
  def texture (name, roof, side)
    
    mats = Sketchup.active_model.materials 
    sideTex = mats.add "Side"
    sideTex.texture = Sketchup.find_support_file side, "Plugins"
    sideTex.texture.size = Shape.getSize(name)
    
    roofTex = mats.add "Roof"
    roofTex.texture = Sketchup.find_support_file roof, "Plugins"
 

    $list.each do |a|
      if a.name == name
        a.entities.each do |b|
          if b.typename == ( "Face" )
              
              if b.normal.z == 1
               b.material = roofTex
               b.back_material = roofTex
               
              else
               
               b.material = sideTex
               b.back_material = sideTex  
              end
          end
        end
      end
    end
    
  end
end