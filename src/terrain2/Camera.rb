class Camera
    $view = Sketchup.active_model.active_view
    
  def setCam(x0, y0, z0, x1, y1, z1)  
   
    eye = [x0, y0, z0]
    target = [x1, y1, z1]
    up = [0, 0, 1]
    cam = Sketchup::Camera.new eye, target, up
    $view.camera = cam
  
  end
  
  def nextFrame()
    new_eye = $view.camera.eye
    new_eye.z = new_eye.z + 1.0
    $view.camera.set(new_eye, $view.camera.target, $view.camera.up)
    $view.show_frame
    return new_eye.z < 500.0
  end
  def animate()
    Sketchup.active_model.active_view.animation = Camera.new
  end
end