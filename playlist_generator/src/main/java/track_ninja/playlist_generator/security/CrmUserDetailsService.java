//package track_ninja.playlist_generator.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.stereotype.Service;
//import track_ninja.playlist_generator.models.User;
//import track_ninja.playlist_generator.repositories.UserRepository;
//
//@Service
//public class CrmUserDetailsService implements UserDetailsService {
//    @Autowired
//    private UserRepository userRepository;
//
////	@Autowired
////	PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
////    	if (userRepository.count() == 0) {
////    		System.out.println("There is no user exist in the database. So, adding two users");
////    		userRepository.save(new User("crmadmin", passwordEncoder.encode("adminpass"), Arrays.asList(new UserRole("USER"), new UserRole("ADMIN"))));
////    		userRepository.save(new User("crmuser", passwordEncoder.encode("crmpass"), Arrays.asList(new UserRole("USER"))));
////    	}
//
//        User user = userRepository.findByUsername(userName);
//        if(user == null){
//            throw new UsernameNotFoundException("UserName "+userName+" not found");
//        }
//        return new CrmUserDetails(user);
//    }
//}
