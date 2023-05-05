package com.techacademy;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    /** 認証・認可設定 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login
            .loginProcessingUrl("/login")    // ユーザー名・パスワードの送信先
            .loginPage("/login")             // ログイン画面
            .defaultSuccessUrl("/index", true) // ログイン成功後のリダイレクト先
            .failureUrl("/login?error")      // ログイン失敗時のリダイレクト先
            .permitAll()                     // ログイン画面は未ログインでアクセス可
        ).logout(logout -> logout
            .logoutSuccessUrl("/login")      // ログアウト後のリダイレクト先
        ).authorizeHttpRequests(auth -> auth
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()                 // css等は未ログインでアクセス可
            .mvcMatchers("/employee/**").hasAuthority("管理者") // 追記部分：従業員管理は管理者のみアクセス可
            .anyRequest().authenticated()    // その他はログイン必要
        );

        //ログインしたときに前のページに飛ぶ仕様になっている→新規のページだと前のページがないのでindex.htmlに飛ぶ仕様（コントローラーを通さない）
        //「/」にアクセスするとindex.htmlが実行されるようになっている→ログイン成功後のリダイレクト先は/でもindex.htmlにアクセスする
        //trueを入れることで、/→index.htmlの動きではなく指定したurlに飛ばすことができる
        //trueを入れたときのデメリット→前のページに飛ばなくなる→途中でコードを変えて更新すると再度ログイン→最初のページに戻ってしまう（作業中のページに戻れない）
        //19行目を/にして、loginControllerのGetMappingを/にする方法もある

        return http.build();
    }

    /** ハッシュ化したパスワードの比較に使用する */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}