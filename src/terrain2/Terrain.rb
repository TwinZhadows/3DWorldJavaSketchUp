
class Terrain
  $faces = []
  $low = 0
  $dist = 0
  $tree = Array.new()
  
  def readFile(path) #read terrain coordinates file
    
    
    pm = Geom::PolygonMesh.new 

    const = 30
    model = Sketchup.active_model
    entities = model.active_entities  
   

    file = File.open path, "r"
    
    lines = file.readlines
    
    
    for line in lines
      buff = line.split(",")
      if buff[0] == "l"
        size = (2**buff[1].to_i)+1
        square = Array.new(size){Array.new(size,0)}
      end
      i = buff[0].to_i
      j = buff[1].to_i
      val = buff[2].to_i
      square[i][j] = val

    end

    0.upto(square.length-2) {|i| 
      0.upto(square.length-2) {|j| 
        iup = i+1
        jright = j+1

        pt0 = Geom::Point3d.new i*const, j*const, square[i][j]
        pt1 = Geom::Point3d.new i*const+const, j*const, square[iup][j]
        pt2 = Geom::Point3d.new i*const, j*const+const, square[i][jright]
        pt3 = Geom::Point3d.new i*const+const, j*const+const,square[iup][jright]

        pm.add_polygon pt0, pt1, pt3
        pm.add_polygon pt0, pt2, pt3

        j+=1
		
	
      }

      i+=1
	
    }
    entities.add_faces_from_mesh pm


  end
  def texture()
 
    face_high = 0
    face_low = 0
    face_low_helper = 0
    model = Sketchup.active_model
    entities = model.active_entities
    #entities = model.selection
    coor = Geom::Point3d.new(0,0,0)

    entities.each do |a|
      if a.typename == ( "Face" )
        facez = a.bounds.center[2]
        if face_low_helper == 0 
          face_low = facez
          $low = facez
          face_low_helper = 1
        end 
        if facez > face_high
          face_high = facez
        end
        if a.bounds.center[2].to_l < face_low
          face_low = facez
          $low = facez
        end
        $faces.push a
      end
    end


    face_dif = face_high - face_low

    dis = face_dif*0.75
    $dist = dis
    dis2 = face_dif*0.4
    dis3 = face_dif*0.07
    dis4 = face_dif* 0.03
    mats = Sketchup.active_model.materials
    grass = mats.add "Grass"
    sand = mats.add "Sand"
    water = mats.add "Water"
    blend = mats.add "Blend"
    blend2 = mats.add "Blend2"

    grass.texture = Sketchup.find_support_file "grass.jpg", "Plugins"
    sand.texture = Sketchup.find_support_file "sand.jpg", "Plugins"
    water.texture = Sketchup.find_support_file "water.jpg", "Plugins"
    blend.texture = Sketchup.find_support_file "blend.jpg", "Plugins"
    blend2.texture = Sketchup.find_support_file "blend2.jpg", "Plugins"

    $faces.each do |a|
	
      cp = a.bounds.center[2] 

      if cp > (face_low + dis)
        a.material = grass
        a.back_material = grass
      else
        if cp < (face_low + dis2)
          a.material = water
          a.back_material = water
        else
          if cp > (face_low + (dis-dis3))
            a.material = blend
            a.back_material = blend
          else
            if cp > (face_low + (dis-dis3-dis4))
              a.material = blend2
              a.back_material = blend2
            else
              a.material = sand
              a.back_material = sand
            end
				
          end
        end    
      end




    end
  end
  
  def addtree ()      
    tree_path = Sketchup.find_support_file "tree.skp", "Components/Objects/"
  
    model = Sketchup.active_model
    treemodel = model.definitions.load tree_path
    $faces.each do |a|
      cp = a.bounds.center[2] 
      p = a.bounds.center
      if cp > ($low + $dist)
        if rand(10) >8
          tr = Geom::Transformation.translation [p[0], p[1], p[2]]
          model.active_entities.add_instance(treemodel, tr)
 
        end
    
        $tree.push a.bounds.center
      
      end
    
    end
  end
end

t = Terrain.new
path = "C:/Users/chon/Documents/NetBeansProjects/terrain2/file.txt"
t.readFile(path)
t.texture()
t.addtree()