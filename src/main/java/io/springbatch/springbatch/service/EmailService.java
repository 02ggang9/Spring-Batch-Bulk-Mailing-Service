package io.springbatch.springbatch.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

//    private static final Pattern PATTERN = Pattern.compile()

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        long startTime = System.currentTimeMillis();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        text = text.replaceAll("\n", "<br>");

        // "{}"와 "{variable}" 형태의 문자열을 찾아서 중괄호를 제거하고 굵게 표시하기 위한 로직
        Pattern pattern = Pattern.compile("\\{([^{}]*)\\}");
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            // matcher.group(1)은 중괄호 내부의 텍스트를 가져옵니다
            matcher.appendReplacement(sb, "<strong>" + matcher.group(1) + "</strong>");
        }
        matcher.appendTail(sb);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("<html><body><div style=\"width:100%;padding:40px 0;background-color:#ffffff;margin:0px auto;display:block\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" style=\"margin:0px auto;width:94%;max-width:630px;border-width:0;border:0;border-style:solid;box-sizing:border-box\"><tbody><tr style=\"margin:0;padding:0\"><td style=\"width:100%;max-width:630px;margin:0 auto;border-spacing:0;border:0;clear:both;border-collapse:separate;padding:0;overflow:hidden\"><div style=\"height:0px;max-height:0px;border-width:0px;border:0px;border-color:initial;line-height:0px;font-size:0px;overflow:hidden;display:none\"> </div><div><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow:hidden;margin:0px auto;padding:0px;width:100%;max-width:630px;clear:both;line-height:1.7;border-width:0px;border:0px;font-size:14px;border:0;box-sizing:border-box\" width=\"100%\"><tbody><tr><td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td style=\"text-align:center;font-size:0\"><div style=\"max-width:630px;width:100%!important;margin:0;vertical-align:top;border-collapse:collapse;box-sizing:border-box;font-size:unset;display:inline-block\"><div style=\"text-align:center;margin:0px;width:100%;box-sizing:border-box;clear:both\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" align=\"center\"><tbody><tr><td style=\"padding:15px 15px 0px 15px;text-align:center;font-size:0;border:0;line-height:0;width:100%;box-sizing:border-box\"><img src=\"https://velog.velcdn.com/images/songs4805/post/d2b40d9a-95a8-4dac-84bf-d4424a7b3684/image.png\" style=\"width:100%;display:inline;vertical-align:bottom;text-align:center;max-width:100%;height:auto;border:0\" width=\"600\" class=\"CToWUd a6T\" data-bit=\"iit\" tabindex=\"0\"><div class=\"a6S\" dir=\"ltr\" style=\"opacity: 0.01; left: 353.906px; top: 134px;\"><div id=\":o2\" class=\"T-I J-J5-Ji aQv T-I-ax7 L3 a5q\" role=\"button\" tabindex=\"0\" aria-label=\"첨부파일() 다운로드\" jslog=\"91252; u014N:cOuCgd,Kr2w4b,xr6bB; 4:WyIjbXNnLWY6MTc4NjYwNTcyNDI0ODIxMzIyOSJd\" data-tooltip-class=\"a1V\" jsaction=\"JIbuQc:.CLIENT\" data-tooltip=\"다운로드\"><div class=\"akn\"><div class=\"aSK J-J5-Ji aYr\"></div></div></div></div><div dir=\"ltr\" style=\"opacity:0.01\"><div id=\"m_303261871990473807:o7\" role=\"button\" aria-label=\"첨부파일() 다운로드\"><div><div></div></div></div></div></td></tr></tbody></table></div></div></td></tr></tbody></table></td></tr></tbody></table></div><div><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow:hidden;margin:0px auto;padding:0px;width:100%;max-width:630px;clear:both;line-height:1.7;border-width:0px;border:0px;font-size:14px;border:0;box-sizing:border-box\" width=\"100%\"><tbody><tr><td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td style=\"text-align:center;font-size:0\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" width=\"100%\">  <tbody><tr>    <td style=\"padding:15px 0 15px 0;border:0\">      <table style=\"width:100%;height:0;background:none;padding:0px;border-top-width:1px;border-top-style:solid;border-top-color:#999;margin:0 0;border-collapse:separate\"></table>    </td>  </tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></div><div><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow:hidden;margin:0px auto;padding:0px;width:100%;max-width:630px;clear:both;line-height:1.7;border-width:0px;border:0px;font-size:14px;border:0;box-sizing:border-box\" width=\"100%\"><tbody><tr><td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td style=\"text-align:center;font-size:0\"><div style=\"max-width:630px;width:100%!important;margin:0;vertical-align:top;border-collapse:collapse;box-sizing:border-box;font-size:unset;display:inline-block\"><div style=\"text-align:left;margin:0px;line-height:1.7;word-break:break-word;font-size:14px;font-family:AppleSDGothic,apple sd gothic neo,noto sans korean,noto sans korean regular,noto sans cjk kr,noto sans cjk,nanum gothic,malgun gothic,dotum,arial,helvetica,MS Gothic,sans-serif!important;color:#333333;clear:both;border:0\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\"><tbody><tr><td style=\"padding:15px 15px 15px 15px;font-size:14px;line-height:1.7;word-break:break-word;color:#333333;border:0;font-family:AppleSDGothic,apple sd gothic neo,noto sans korean,noto sans korean regular,noto sans cjk kr,noto sans cjk,nanum gothic,malgun gothic,dotum,arial,helvetica,MS Gothic,sans-serif!important;width:100%\"><p style=\"text-align:left\"><span style=\"font-size:10.5pt;color:#333333\"></p>\n" + "<p>" + sb.toString() + "</p>" + "</div><div style=\"text-align:left;color:#333333;font-size:14px\"><br></div></td></tr></tbody></table></div></div></td></tr></tbody></table></td></tr></tbody></table></div><div><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow:hidden;margin:0px auto;padding:0px;width:100%;max-width:630px;clear:both;line-height:1.7;border-width:0px;border:0px;font-size:14px;border:0;box-sizing:border-box\" width=\"100%\"><tbody><tr><td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td style=\"text-align:center;font-size:0\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" width=\"100%\">  <tbody><tr>    <td style=\"padding:15px 15px 15px 15px;border:0\">      <table style=\"width:100%;height:0;background:none;padding:0px;border-top-width:1px;border-top-style:solid;border-top-color:#999;margin:0 0;border-collapse:separate\"></table>    </td>  </tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></div><div><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow:hidden;margin:0px auto;padding:0px;width:100%;max-width:630px;clear:both;line-height:1.7;border-width:0px;border:0px;font-size:14px;border:0;box-sizing:border-box\" width=\"100%\"><tbody><tr><td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td style=\"text-align:center;font-size:0\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" width=\"100%\"><tbody><tr><td style=\"padding:15px 15px 15px 15px;border:0\"><div style=\"margin:0 auto;font-family:AppleSDGothic,apple sd gothic neo,noto sans korean,noto sans korean regular,noto sans cjk kr,noto sans cjk,nanum gothic,malgun gothic,dotum,arial,helvetica,MS Gothic,sans-serif;text-align:center\"><span style=\"list-style:none;padding:0 10px 0 0;margin:0px\"><a href=\"https://event.stibee.com/v2/click/NDg3ODgvMTkzNDY1NS8xMjA1Mzkv/aHR0cHM6Ly93d3cuZmFjZWJvb2suY29tL3dvb3dhaGFuVGVjaC8\" style=\"padding:0px 0px;border-width:0px;text-decoration:none;display:inline-block;color:rgb(0,0,255);font-weight:bold\" title=\"페이스북\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://event.stibee.com/v2/click/NDg3ODgvMTkzNDY1NS8xMjA1Mzkv/aHR0cHM6Ly93d3cuZmFjZWJvb2suY29tL3dvb3dhaGFuVGVjaC8&amp;source=gmail&amp;ust=1703926394306000&amp;usg=AOvVaw3pCEIqXQhyKyE-_e1k-PO2\"><img src=\"https://ci3.googleusercontent.com/meips/ADKq_NYToEwu8draMmywEvdD-vdW9CK2TwkIxy1ZljoeWidM6P5mrPExKKVBqtZJgtsvy8UzuBeEdWDV6G5OfHBLBkvahYrX1na9WQ5NJgoCKw4uiKIL=s0-d-e1-ft#https://resource.stibee.com/editor/icon/sns/facebook-snsB.png\" style=\"height:30px;width:auto;vertical-align:middle\" height=\"30\" width=\"30\" class=\"CToWUd\" data-bit=\"iit\"></a></span><span style=\"list-style:none;padding:0 10px 0 10px;margin:0px\"><a href=\"https://velog.io/@bdd14club/posts\" style=\"padding:0px 0px;border-width:0px;text-decoration:none;display:inline-block;color:rgb(0,0,255);font-weight:bold\" title=\"블로그\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://event.stibee.com/v2/click/NDg3ODgvMTkzNDY1NS8xMjA1Mzkv/aHR0cHM6Ly90ZWNoYmxvZy53b293YWhhbi5jb20v&amp;source=gmail&amp;ust=1703926394306000&amp;usg=AOvVaw3uajreVkMM44T_oxj6faPV\"><img src=\"https://ci3.googleusercontent.com/meips/ADKq_NYfNXHZpXm7fpsuAP5h2bzSK7nbNLqkwyb5-Wph4TCFsnkRBGltxvxT3UGWMjNrEN6ZF50HkD3gelESBKREiz22Qc-nDuOZyktsWikp6yA=s0-d-e1-ft#https://resource.stibee.com/editor/icon/sns/blog-snsB.png\" style=\"height:30px;width:auto;vertical-align:middle\" height=\"30\" width=\"30\" class=\"CToWUd\" data-bit=\"iit\"></a></span><span style=\"list-style:none;padding:0 0 0 10px;margin:0px\"><a href=\"https://www.youtube.com/@BDD_CLUB\" style=\"padding:0px 0px;border-width:0px;text-decoration:none;display:inline-block;color:rgb(0,0,255);font-weight:bold\" title=\"유튜브\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://event.stibee.com/v2/click/NDg3ODgvMTkzNDY1NS8xMjA1Mzkv/aHR0cHM6Ly93d3cueW91dHViZS5jb20vQHdvb3dhdGVjaA&amp;source=gmail&amp;ust=1703926394306000&amp;usg=AOvVaw26GorHGGIoVQ4PudlRInpj\"><img src=\"https://ci3.googleusercontent.com/meips/ADKq_NZ8ove_ZEUBOOlrn4lj-nQci3Cgtif9gnv8drK6TQc_WNl351VIJrRyY32YK42VuPNZRjWkxMUhmqJz5BNgqNoKrvpJ33z7p8io1Ep1K5dpz2k=s0-d-e1-ft#https://resource.stibee.com/editor/icon/sns/youtube-snsB.png\" style=\"height:30px;width:auto;vertical-align:middle\" height=\"30\" width=\"30\" class=\"CToWUd\" data-bit=\"iit\"></a></span></div></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></div></td></tr></tbody></table></div>\n</body></html>", true);

        javaMailSender.send(message);

        long endTime = System.currentTimeMillis();
        System.out.println("이메일 전송 시간: " + (endTime - startTime));
    }

}
