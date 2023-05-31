pm = Geom::PolygonMesh.new 
square = Array.new(33){Array.new(33,0)}
model = Sketchup.active_model
entities = model.active_entities
file = File.open "D:/file.txt", "r"
lines = file.readlines
const = 30
tree = model.definitions.load( "tree.skp" )

def add_tree (p)

	if rand(10) >7
 	   tr = Geom::Transformation.translation[p[0], p[1], p[2]]
	   model.active_entities.add_instance( tree, tr )
	end
	
end

t1 = Time.now
for line in lines
	buff = line.split(",")
	i = buff[0].to_i
	j = buff[1].to_i
	val = buff[2].to_i
	square[i][j] = val
	#puts [i,",",j,"=",square[i][j]]
	#point = Geom::Point3d.new(i*const,j*const,square[i][j])
	#entities.add_cpoint point
end


0.upto(square.length-2) {|i| 
	0.upto(square.length-2) {|j| 
		iup = i+1
		jright = j+1

		pt0 = Geom::Point3d.new i*const, j*const, square[i][j]
		pt1 = Geom::Point3d.new i*const+const, j*const, square[iup][j]
		pt2 = Geom::Point3d.new i*const, j*const+const, square[i][jright]
		pt3 = Geom::Point3d.new i*const+const, j*const+const,square[iup][jright]

		pm.add_polygon pt0, pt1, pt2
		pm.add_polygon pt3, pt1, pt2

		j+=1
		
	
	}

	i+=1
	
}
entities.add_faces_from_mesh pm

#======================== Texture ==============================

      faces = []
      face_high = 0
      face_low = 0
      face_low_helper = 0
      values = [0,0,0]
	 results1 = [0,0,0]
	 results2 = [255,255,255]
	 #entities = model.selection

      entities.each do |a|
        if a.typename == ( "Face" )
          facez = a.bounds.center[2]
          if face_low_helper == 0 
            face_low = facez
            face_low_helper = 1
          end 
          if facez > face_high
            face_high = facez
          end
          if a.bounds.center[2].to_l < face_low
            face_low = facez
          end
          faces.push a
        end
      end

      rdif = results1[0] - results2[0]
      gdif = results1[1] - results2[1]
      bdif = results1[2] - results2[2]
      face_dif = face_high - face_low
      rbase = results2[0]
      gbase = results2[1]
      bbase = results2[2]

	 dis = face_dif*0.75
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

	 faces.each do |a|
	
        cp = a.bounds.center[2] 

        #if rdif == 0
        #  r = results1[0]
        #else
        #  r = ((cp*100 / face_dif) * rdif / 100) + rbase
        #end
        #if gdif == 0
        #  g = results1[1]
        #else
        #  g = ((cp*100 / face_dif) * gdif / 100) + gbase
        #end
        #if bdif == 0
        #  b = results1[2]
        #else
        #  b = ((cp*100 / face_dif) * bdif / 100) + bbase
        #end
	
	   if cp > (face_low + dis)
	   	    a.material = grass
        	    a.back_material = grass
		    add_tree(a.bounds.center)	 
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


        #a.material = [r.to_i, g.to_i ,b.to_i]
        #a.back_material = [r.to_i, g.to_i ,b.to_i]


      end
t2 = Time.now
secs = t2-t1
puts secs
	
