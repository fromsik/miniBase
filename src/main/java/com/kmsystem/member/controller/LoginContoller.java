package com.kmsystem.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
@ApiIgnore
public class LoginContoller {
		
//	private final JwtTokenProvider jwtTokenProvider;
//	private final AuthenticationManager authenticationManager;
    
	@GetMapping("/logoutSuccess")
	public ResponseEntity<String> logout(){
		 
		return new ResponseEntity<>("logout test",HttpStatus.OK);
	}
	
	
    /*
    @PostMapping("/login")
    public ResponseEntity<Void> attemptAuthentication(@RequestBody Employee employee, HttpServletResponse response) throws AuthenticationException {
    	
    	
    	String username = employee.getEmailId();
    	String password = employee.getEmpPw();
       
    	Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        authenticationToken = authenticationManager.authenticate(authenticationToken);       
        CustomUser user = ((CustomUser) authenticationToken.getPrincipal());
        
        int EmpId = user.getEmpId();
        String EmailId = user.getEmailId();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        int cmId = user.getCmId();
        
        String token = jwtTokenProvider.createToken(EmpId, EmailId, roles, cmId);

        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
	*/

}
