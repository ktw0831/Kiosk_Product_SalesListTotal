package a_010_java_after2;

import java.sql.*;
import java.util.*;

class Product_Sales_Kiosk_Total{
    public int     ord_pdt_id;				
    public int     ord_buying_count;		
    public int     ord_price;	
    public String pdt_id_name;	    
    public int     cnt;						
    
    void printScore() {
        System.out.printf("%3d   %8d   %8d   %9d   %s%n",
                cnt, ord_pdt_id, ord_buying_count, ord_price, pdt_id_name);
    }
}
public class Kiosk_Product_SalesListTotal {

    public static void main(String[] args) {
    	Scanner sc=new Scanner(System.in);
    	int Sales=0;
    	
        int num_count =0;
        do {
    
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql;
       
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String id = "system";
        String pw = "1234";
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("클래스 로딩 성공");
            conn = DriverManager.getConnection(url, id, pw);
            System.out.println("DB 접속");
            sql="select count(*) num from tbl_order_total";
           
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            
            num_count = rs.getInt("num");
            System.out.println("등록된코드:"+num_count+"건");
           
            System.out.println("상품코드를 입력하세요. 주문 리스트를 출력합니다. 전체:1 종료:9");
            Sales=sc.nextInt();
            
            if(Sales==1) {
            	sql= "select ord_pdt_id, sum(ord_buying_count) ord_buying_count, sum(ord_price) ord_price, pdt_id_name"
                        + " from  tbl_product_master a,  tbl_order_list b, tbl_order_total c"
                        + " where pdt_id = b.ord_pdt_id and b.ord_no = c. tot_ord_no"
                        + " group by pdt_id_name, ord_pdt_id";
            }else if(Sales==9) {
            	Kiosk_MainMenu.main(args);
                break;
            }else if (Sales != 1 || Sales != 9) {
            	sql="select ord_pdt_id, sum(ord_buying_count) ord_buying_count, sum(ord_price) ord_price, pdt_id_name "
                        + "from  tbl_product_master a,  tbl_order_list b, tbl_order_total c "
                        + "where ord_pdt_id="+Sales+" and a.pdt_id = b.ord_pdt_id and b.ord_no = c. tot_ord_no "	
                        + "group by pdt_id_name, ord_pdt_id "
                        + "order by ord_pdt_id";
                }

            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            System.out.println("================================================");
            System.out.println(" 순번    상품코드       수량        금액       상품이름");
            System.out.println("================================================");

                num_count = 0;
                int total=0;
                int buying_count=0;
                Product_Sales_Kiosk_Total p =new Product_Sales_Kiosk_Total();
                while(rs.next()) {
                    p.cnt = num_count+1;
                    num_count++;
                    p.ord_pdt_id = rs.getInt("ord_pdt_id");
                    p.ord_buying_count = rs.getInt("ord_buying_count");
                    p.ord_price = rs.getInt("ord_price");
                    p.pdt_id_name = rs.getString("pdt_id_name");
                    p.printScore();
                    
                    total = total + p.ord_price;
                    buying_count = buying_count + p.ord_buying_count;
                }
                
                System.out.println("================================================");
                System.out.println("*전체판매출합계: "+buying_count+"  "+ total);
                																							
    }catch(Exception e) {																						
        e.printStackTrace();
    }       
        continue;
        }while(true);																						
   }
}