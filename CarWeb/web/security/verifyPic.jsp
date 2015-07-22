<%@ page import="java.awt.*, java.awt.image.BufferedImage,java.util.*,
com.sun.image.codec.jpeg.*,java.io.OutputStream" %><%
    OutputStream jpegOut = response.getOutputStream();
    response.setContentType("image/jpeg");
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires", 0);
    int width = 330, height = 70;
    BufferedImage image = new BufferedImage(width, height,
      BufferedImage.TYPE_INT_RGB);
    // 获取图形上下文
    Graphics g = image.getGraphics();
    // 生成随机类
    Random random = new Random();
    // 设定背景色
    g.setColor(getRandColor(200, 250));
    g.fillRect(0, 0, width, height);
    // 设定字体
    //g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
    g.setFont(new Font("Times New Roman", Font.PLAIN, 60));
    // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
    g.setColor(getRandColor(180, 200));
/*
    for (int i = 0; i < 155; i++) {
     int x = random.nextInt(width);
     int y = random.nextInt(height);
     int xl = random.nextInt(12);
     int yl = random.nextInt(12);
     g.drawLine(x, y, x + xl, y + yl);
    }
*/
    // 取随机产生的认证码(4位数字)
    String sRand =setRandValue(request);
    if(sRand!=null)
    for (int i = 0; i < sRand.length(); i++) {
     g.setColor(new Color(20 + random.nextInt(200), 20 + random
       .nextInt(200), 20 + random.nextInt(200)));
     // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
     //g.drawString(sRand.charAt(i) + "", 13 * i + 6, 16);
        g.drawString(sRand.charAt(i) + "", width/sRand.length() * i + 16, height-8);
    }

    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(jpegOut);
    encoder.encode(image);
    // 图象生效
    g.dispose();
    // 输出图象到页面
    // ImageIO.write(image, "JPG", response.getOutputStream());
    jpegOut.flush();
    jpegOut.close();
%><%!

 Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
  Random random = new Random();
  if (fc > 255)
   fc = 255;
  if (bc > 255)
   bc = 255;
  int r = fc + random.nextInt(bc - fc);
  int g = fc + random.nextInt(bc - fc);
  int b = fc + random.nextInt(bc - fc);
  return new Color(r, g, b);
 }
 //48-57对应0-9;65-90对应A-Z;97-122对应a-z;
 String setRandValue(HttpServletRequest request) {
  Random random = new Random();
  String sRand = "";
  //这里是生成验证码的位数
  for (int i = 0; i < 4; i++) {
   char c = 0;
   int k = random.nextInt(3);
   switch (k) {
   case 0:
    c = (char) (random.nextInt(10) + 48);
    break;
   case 1:
    c = (char) (random.nextInt(26) + 65);
    break;
   case 2:
    c = (char) (random.nextInt(26) + 97);
   }
      if(c =='1'){
          c = '8';
      }else if(c == 'l'){
          c = '6';
      }

   sRand += c;
  }
  request.getSession().setAttribute("verifyCode", sRand);
  return sRand;
 }
%>