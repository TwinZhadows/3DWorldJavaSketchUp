	count = 0
	while(count <10)

	  t0 = Time.now
	  while (Time.now - t0 <0.1)
		#wait	
	  end
	  puts 1
	  count +=1
	end	