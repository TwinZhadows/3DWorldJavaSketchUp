
class Terrain
  $faces = []
  $low = 0

  $tree = Array.new()
  file = File.open "C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/objects.txt", 'w'
  file.close
  $level 
  $tex
  $dist
  $totalDist = 0
  
  def readFile(path, dist) #read terrain coordinates file
    
    
    pm = Geom::PolygonMesh.new 

    const = dist
    model = Sketchup.active_model
    entities = model.active_entities  
   

    file = File.open path, "r"
    
    lines = file.readlines
    
    
    for line in lines
      buff = line.split(",")
      if buff[0] == "l"
        size = buff[1].to_i
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
  
  def setLevel(num)
    $level = num
    $dist = Array.new(num)
    $tex = Array.new(num)
  end
  
  def setTexture(index, tex, height)
    mats = Sketchup.active_model.materials 
    newtex = mats.add "Texture"
    newtex.texture = Sketchup.find_support_file tex, "Plugins"
    $tex[index] = newtex
    $dist[index] = height
    $totalDist += height
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
    
    0.upto($dist.length-1){|i|
        tmp = $dist[i]*(face_high - face_low)/$totalDist
        $dist[i] = tmp
        puts tmp
  
    }
    upper = face_high

    $faces.each do |a|
        cp = a.bounds.center[2] 
        
          if cp == face_high
              a.material = $tex[$level-1]
              a.back_material = $tex[$level-1]
          elsif cp == face_low
              a.material = $tex[0]
              a.back_material = $tex[0]
          else
            
            ($dist.length-1).downto(0) {|i| 
              if cp < upper
               if cp >= (upper - $dist[i])
                 a.material = $tex[i]
                 a.back_material = $tex[i]
                 
               end
              end 
              upper -= $dist[i]
            }
            upper = face_high  
          end
          
          
    end


  end
  def addModel (name, scale, rotation, x, y, z)    
    
    path = Sketchup.find_support_file name, "Components/Objects/"
    file = File.open "C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/objects.txt", 'a'
    model = Sketchup.active_model
    
    r = Geom::Transformation.rotation [x,y,z],[0,0,1],rotation.degrees
    sc = Geom::Transformation.scaling scale, scale, scale 
    tr = Geom::Transformation.translation [x, y, z]
    componentdefinition = model.definitions.load path
    obj = model.active_entities.add_instance componentdefinition, tr*sc
    box = obj.bounds
    #rotate after so that the bounding box doesn't change
    obj.transform! r
    file.write("#{box.width.to_i}, #{box.height.to_i}\n")
    
    file.close
    puts "#{box.width.to_i}, #{box.height.to_i}\n"
    #$tree.push a.bounds.center
    
    
  end
  
  def setCam(x0, y0, z0, x1, y1, z1)
    
    view = Sketchup.active_model.active_view
    eye = [x0, y0, z0]
    target = [x1, y1, z1]
    up = [0, 0, 1]
    cam = Sketchup::Camera.new eye, target, up
    view.camera = cam
  
  end
 
    
  def createTerrain(file, size, dist )

    pm = Geom::PolygonMesh.new 

    model = Sketchup.active_model
    entities = model.active_entities  
      
      
    dib = GL_BMP.new( file )
    const = dist#argument
    dim = 2**size + 1
    square = Array.new(dim){Array.new(dim,0)}#argument
    # Dimensions
    step_x = 1
    step_y = 1
    step_z = 1
    # Process data
    puts 'Bitmap To Mesh:'
    start_time = Time.now
    # Read colour values and generate 3d points.
    pts = []
    progress = TT::Progressbar.new( dib.pixels, 'Reading Image' )
    dib.width.times { |x|
      dib.height.times { |y|
        progress.next
        index = (dib.width * y) + x
        color = dib.data[index]
        # Generate a Point3d from pixel colour.
        r,g,b = color
        if r == g && g == b
          average_color = r
        else
          # http://forums.sketchucation.com/viewtopic.php?t=12368#p88865
          average_color = (r * 0.3) + (g * 0.59) + (b * 0.11);
        end
        ptx = step_x * x
        pty = step_y * y
        ptz = step_z * average_color
        square[x][y] = ptz
        pts << Geom::Point3d.new( [ptx, pty, ptz] )
              
      }
    }
      
    total = pts.size.to_f
    puts "> Processing data took: #{Time.now - start_time}s"
    puts total
    #puts pts
      
      
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
end

module GL_DIB
  attr_accessor(:width, :height, :data)
    
  def initialize(filename)
    @data = read_image(filename)
  end
    
  def pixels
    @width * @height
  end
    
end
  
class GL_BMP
  include GL_DIB
    
  # http://en.wikipedia.org/wiki/BMP_file_format
  #
  # http://www.herdsoft.com/ti/davincie/imex3j8i.htm
  # http://www.digicamsoft.com/bmp/bmp.html
  # http://netghost.narod.ru/gff/graphics/summary/os2bmp.htm
  # http://atlc.sourceforge.net/bmp.html#_toc381201084
  #
  # http://msdn.microsoft.com/en-us/library/dd183386%28VS.85%29.aspx
  # http://msdn.microsoft.com/en-us/library/dd183380%28VS.85%29.aspx
  # http://msdn.microsoft.com/en-us/library/dd183381%28VS.85%29.aspx
  #
  # http://entropymine.com/jason/bmpsuite/
  # http://wvnvaxa.wvnet.edu/vmswww/bmp.html
  #
  # uint32_t - DWORD - V
  # uint16_t -  WORD - v
  #
  # BMP File Header     Stores general information about the BMP file.
  # Bitmap Information  Stores detailed information about the bitmap image. (DIB header)
  # Color Palette       Stores the definition of the colors being used for indexed color bitmaps.
  # Bitmap Data         Stores the actual image, pixel by pixel.

  # DIB Header Size
  BITMAPCOREHEADER  =  12 # OS/2 V1
  BITMAPCOREHEADER2 =  64 # OS/2 V2
  BITMAPINFOHEADER  =  40 # Windows V3
  BITMAPV4HEADER    = 108 # Windows V4
  BITMAPV5HEADER    = 124 # Windows V5
  # Compression
  BI_RGB       = 0
  BI_RLE8      = 1
  BI_RLE4      = 2
  BI_BITFIELDS = 3
  BI_JPEG      = 4
  BI_PNG       = 5
    
  # This method silently fails when encountering errors. The error message is sent to the
  # console.
  #
  # Returns array of each pixel ( Array<Point3d, color> )
  def read_image(filename)
    #puts "\nReading BMP: '#{File.basename(filename)}' ..."
      
    file = File.open(filename, 'rb')
      
    # BMP File Header
    bmp_magic = file.read(2)
    raise 'BMP Magic Marker not found.' if bmp_magic != 'BM'
    bmp_header = file.read(12).unpack('VvvV')
    filesz, creator1, creator2, bmp_offset = bmp_header
        
      
    # DIB Header
    # Read the first uint32_t that gives the size of the DIB header and use that to determine
    # which DIB header this BMP uses.
    #
    # (!) Try to read V4 & V5 as BITMAPINFOHEADER. Seek to data start.
    header_sz = file.read(4).unpack('V').first
    case header_sz
    when BITMAPCOREHEADER
      dib_header = file.read(8).unpack('vvvv')
      @width, @height, nplanes, bitspp = dib_header
    when BITMAPCOREHEADER2
      raise "Unsupported DIB Header. (Size: #{header_sz})"
    when BITMAPINFOHEADER
      # (!) l to read signed 4 byte integer LE does not work on PPC Mac.
      #dib_header = file.read(36).unpack('llvvVVllVV')
      dib_header = file.read(36).unpack('VVvvVVVVVV') # work for the types bundles with the plugin
      @width, @height, nplanes, bitspp, compress_type, bmp_bytesz,
        hres, vres, ncolors, nimpcolors = dib_header
    when BITMAPV4HEADER
      raise "Unsupported DIB Header. (Size: #{header_sz})"
    when BITMAPV5HEADER
      raise "Unsupported DIB Header. (Size: #{header_sz})"
    else
      raise "Unknown DIB Header. (Size: #{header_sz})"
    end
    #puts dib_header.inspect
      
    # Verify the supported compression
    unless compress_type.nil? || compress_type == BI_RGB
      raise "Unsupported Compression Type. (type: #{compress_type})"
    end
      
    # Color Palette
    if bitspp < 16
      palette = []
      # Unless the DIB header specifies the colour count, use the max
      # palette size.
      if ncolors.nil? || ncolors == 0
        case bitspp
        when 1
          ncolors = 2
        when 4
          ncolors = 16
        when 8
          ncolors = 256
        else
          raise "Unknown Color Palette. #{bitspp}"
        end
      end
      ncolors.times { |i|
        if header_sz == BITMAPCOREHEADER
          palette << file.read(3).unpack('CCC').reverse!
        else
          b,g,r,a = file.read(4).unpack('CCCC')
          palette << [r,g,b]
        end
      }
      #puts palette.inspect
    end
      
    # Bitmap Data
    #data = Hash.new { |hash, key| hash[key] = [] }
    data = []
    row = y = x = 0
    r, g, b, a, c, n = nil
    while row < @height.abs
      # Row order is flipped if @height is negative. 
      y = (@height < 0) ? row : @height.abs-1-row
      x = 0
      while x < @width.abs
        case bitspp
        when 1
          i = file.read(1).unpack('C').first
          8.times { |n|
            #data[ palette[(i & 0x80 == 0) ? 0 : 1] ] << Geom::Point3d.new(x+n,y,0)
            data << palette[(i & 0x80 == 0) ? 0 : 1]
            break if x+n == @width-1
            i <<= 1
          }
          x += 7
        when 4
          i = file.read(1).unpack('C').first
          #data[ palette[(i>>4) & 0x0f] ] << Geom::Point3d.new(x,y,0)
          data << palette[(i>>4) & 0x0f] 
          x += 1
          #data[ palette[i & 0x0f] ] << Geom::Point3d.new(x,y,0) if x < @width
          data << palette[i & 0x0f] if x < @width
        when 8
          i = file.read(1).unpack('C').first
          #data[ palette[i] ] << Geom::Point3d.new(x,y,0)
          data << palette[i]
        when 16
          c = file.read(2).unpack('v').first
          r = ((c >> 10) & 0x1f) << 3
          g = ((c >>  5) & 0x1f) << 3
          b = (c >> 0x1f) << 3
          #data[ [r,g,b] ] << Geom::Point3d.new(x,y,0)
          data << [r,g,b]
        when 24
          #data[ file.read(3).unpack('CCC').reverse! ] << Geom::Point3d.new(x,y,0)
          data << file.read(3).unpack('CCC').reverse!
        when 32
          b,g,r,a = file.read(4).unpack('CCCC')
          #data[ [r,g,b] ] << Geom::Point3d.new(x,y,0)
          data << [r,g,b]
        else
          raise "UNKNOWN BIT DEPTH! #{bitspp}"
        end
          
        x += 1
      end
      # Skip trailing padding. Each row fills out to 32bit chunks
      # RowSizeTo32bit - RowSizeToWholeByte
      file.seek( (((@width*bitspp / 8) + 3) & ~3) - (@width*bitspp / 8.0).ceil, IO::SEEK_CUR)
        
      row += 1
    end
    #puts "> EOF: #{file.eof?.inspect} - Pos: #{file.pos} / #{filesz}\n\n"
  rescue => e
    puts "Failed to read #{filename}"
    puts e.message
    puts e.backtrace
    #data = {}
    data = []
  ensure
    file.close
    return data
  end


end # class GL_BMP

