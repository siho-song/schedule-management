package show.schedulemanagement.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import show.schedulemanagement.domain.auth.MemberDetails;
import show.schedulemanagement.exception.AuthException;
import show.schedulemanagement.exception.ExceptionCode;
import show.schedulemanagement.service.auth.MemberDetailsService;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MemberDetailsService memberDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        MemberDetails member = (MemberDetails) memberDetailsService.loadUserByUsername(email);
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new AuthException(ExceptionCode.INVALID_PASSWORD);
        }

        return new UsernamePasswordAuthenticationToken(member, password, member.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
