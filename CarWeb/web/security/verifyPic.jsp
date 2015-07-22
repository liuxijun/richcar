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
    // ��ȡͼ��������
    Graphics g = image.getGraphics();
    // ���������
    Random random = new Random();
    // �趨����ɫ
    g.setColor(getRandColor(200, 250));
    g.fillRect(0, 0, width, height);
    // �趨����
    //g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
    g.setFont(new Font("Times New Roman", Font.PLAIN, 60));
    // �������155�������ߣ�ʹͼ���е���֤�벻�ױ���������̽�⵽
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
    // ȡ�����������֤��(4λ����)
    String sRand =setRandValue(request);
    if(sRand!=null)
    for (int i = 0; i < sRand.length(); i++) {
     g.setColor(new Color(20 + random.nextInt(200), 20 + random
       .nextInt(200), 20 + random.nextInt(200)));
     // ���ú�����������ɫ��ͬ����������Ϊ����̫�ӽ�������ֻ��ֱ������
     //g.drawString(sRand.charAt(i) + "", 13 * i + 6, 16);
        g.drawString(sRand.charAt(i) + "", width/sRand.length() * i + 16, height-8);
    }

    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(jpegOut);
    encoder.encode(image);
    // ͼ����Ч
    g.dispose();
    // ���ͼ��ҳ��
    // ImageIO.write(image, "JPG", response.getOutputStream());
    jpegOut.flush();
    jpegOut.close();
%><%!

 Color getRandColor(int fc, int bc) {// ������Χ��������ɫ
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
 //48-57��Ӧ0-9;65-90��ӦA-Z;97-122��Ӧa-z;
 String setRandValue(HttpServletRequest request) {
  Random random = new Random();
  String sRand = "";
  //������������֤���λ��
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