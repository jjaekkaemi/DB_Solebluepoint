import java.sql.*;
import java.util.Scanner;
import java.io.*;

public class testMain {
static String url;
public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   PreparedStatement pstmt = null;
   Scanner scan = new Scanner(System.in) ;
   ResultSet rs = null ;
   ResultSet rs2 = null ;
   ResultSet rs3 = null ;
   ResultSet rs4 = null ;
   int count, count2, count3 ;
   int input ;
   float sum ;
	String searching ;
   try {
       Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); // JDBC 드라이버 로드
        conn = DriverManager.getConnection("jdbc:mysql://172.17.222.233:3306/DBclass?serverTimezone=UTC", "yc12312", "yc9639631!");//URL,UID,PWD        
        if(conn==null){
            System.out.println("연결실패");
        }else{
        	System.out.println("연결성공");
        	
        	stmt = conn.createStatement() ;
            rs= stmt.executeQuery("SELECT * FROM movie order by rand() limit 1 ;") ;
            System.out.println(rs.getInt("movie_id") + " " 
       			 + rs.getString("movie_name") + " "
            	 + rs.getTimestamp("movie_year") + " "
            	 + rs.getTimestamp("movie_date") + " "
            	 + rs.getString("movie_country") + " "
            	 + rs.getInt("movie_boxOffice") + " "
            	 + rs.getString("movie_description") + " "
            	 + rs.getInt("movie_runningTime") + " "
            	 + rs.getString("movie_title") + " "
            	 + rs.getInt("movie_play") + " ") ;
        	while(true) {
        		System.out.println("1. Movie ");
        		System.out.println("2. Person ");
        		System.out.println("3. User ");
        		input = scan.nextInt() ;
        		if(input==1) {
        			System.out.print("입력 : ");
        			searching = scan.next();
        			rs = stmt.executeQuery("SELECT * FROM movie WHERE movie_name = " + searching + ";" ) ;
        			System.out.println(rs.getInt("movie_id") + " " 
        	       			 + rs.getString("movie_name") + " "
        	            	 + rs.getTimestamp("movie_year") + " "
        	            	 + rs.getTimestamp("movie_date") + " "
        	            	 + rs.getString("movie_country") + " "
        	            	 + rs.getInt("movie_boxOffice") + " "
        	            	 + rs.getString("movie_description") + " "
        	            	 + rs.getInt("movie_runningTime") + " "
        	            	 + rs.getString("movie_title") + " "
        	            	 + rs.getInt("movie_play") + " ") ;
        			break ;
        		}
        		else if(input==2) {
        			System.out.print("입력 : ");
        			searching = scan.next();
        			rs = stmt.executeQuery("SELECT m.* From movie AS m "
        					+ "JOIN person_list AS p"
        					+ " ON p.person_name = " + searching 
        					+ " JOIN cast_list AS c "
        					+ "ON p.person_id = c.cast_person_id AND c.cast_movie_id = m.movie_id;") ;
        			moviePrint(rs) ;
        			break ;
        		}
        		else if(input==3) {
        			System.out.print("입력 : ");
        			searching = scan.next();
        			rs = stmt.executeQuery("SELECT UserList.*, evaluate_movie.star_point, movie.* FROM UserList "
        					+ "INNER JOIN evaluate_movie "
        					+ "ON UserList.User_id ="  + searching +
        								" AND UserList.User_id = evaluate_movie.eval_user_id INNER JOIN movie " + 
        								"ON evaluate_movie.eval_movie_id = movie.movie_id ;") ;
        			count = 0 ;
        			sum = 0 ;
        			while(rs.next()){
        				sum += rs.getInt("star_point") ;
        				count++ ;
        			}
        			sum /= (float)count ;
        			System.out.println(rs.getString("User_name") + " " + "평가 영화 별점 : " + sum) ;
        			System.out.println("평가한 영화 리스트");
        			while(rs.next()) System.out.println(rs.getInt("movie_id") + " " + rs.getString("movie_name")
        												+ " " + rs.getFloat("star_point"));
        			System.out.println("1. follow \n 2. Watch Partner \n 3. evaulated movie list ");
        			System.out.print("선택 : ");
        			input = scan.nextInt() ;
        			int userId = 21000261 ;
        			switch(input) {
						case 1 :
							rs2 = stmt.executeQuery("SELECT * From followList ;") ;
							if(rs2.getInt("Following_User_Id")==userId && rs.getInt("User_id")==rs2.getInt("Followed_User_Id")) {//팔로우 일치 하는지 검사
								System.out.println("팔로우 삭제하시겠습니까? N/Y") ;
								searching = scan.next();
								if(searching.equalsIgnoreCase("Y")) {
									stmt.executeUpdate("DELETE FROM followList WHERE Following_User_Id =" +userId + " = " + rs.getInt("User_id") + ";" ) ;
								}
							}
							else {
								System.out.println("팔로우 추가하시겠습니까? N/Y") ;
								searching = scan.next();
								if(searching.equalsIgnoreCase("Y")) {
									stmt.executeUpdate("INSERT INTO followList (Following_User_Id, Followed_User_Id) VALUES (" + userId +", " + rs.getInt("User_id") + ") ;") ;
								}
							}
							break ;
						case 2 :
							rs2 = stmt.executeQuery("SELECT * From UserList WHERE User_id = " + userId +" ;") ;
							if(rs2.getInt("Partener_User_Id")==rs.getInt("User_id")) {
								System.out.println("파트너를 끊으시겠습니까? N/Y") ;
								searching = scan.next();
								if(searching.equalsIgnoreCase("Y")) {
									stmt.executeUpdate("UPDATE UserList SET Partener_User_Id = 0 WHERE User_id = " + rs2.getInt("User_Id") + ";" ) ;
								}
							}
							else stmt.executeUpdate("UPDATE UserList SET Partener_User_Id = " + rs.getInt("User_Id") + " WHERE User_id = " + rs2.getInt("User_Id") + ";" ) ;
							break ;
						case 3 : 
							while(rs.next()) {
								System.out.println(rs.getInt("movie_id")+" " + rs.getString("movie_name")) ;
							}
							break ;
        			}
        			
        		}
        	}
        	
        	rs = stmt.executeQuery("SELECT * FROM collection;") ;
        	while(rs.next()) System.out.println(rs.getInt("collection_id")+ " " + rs.getString("collection_name")) ;
        	System.out.println("원하는 컬렉션 번호 입력");
        	input = scan.nextInt() ;
        	rs = stmt.executeQuery("SELECT c.*, m.movie_id, m.movie_name FROM collection AS c "
        			+ "INNER JOIN collectionMovie AS cm"
        			+ " ON c.collection_id = " + input + " AND c.collection_id = cm.collection_id"
        			+ " INNER JOIN movie AS m"
        			+ " ON cm.movie_id = m.movie_id ;");//SELECT c.*, m.movie_id, m.movie_name FROM collection AS c INNER JOIN collectionMovie AS cm ON c.collection_id = 1 AND c.collection_id = cm.collection_id INNER JOIN movie AS m ON cm.movie_id = m.movie_id ;
        	rs2 = stmt.executeQuery("SELECT c.collection_id, cc.*, u.User_name FROM collection AS c "
        			+ "INNER JOIN collectionComments AS cc "
        			+ "ON c.collection_id = " + input + " AND c.collection_id = cc.collection_id"
        			+ "INNER JOIN UserList As u"
        			+ "ON u.user_id=cc.user_id;") ;
        	rs3 = stmt.executeQuery("SELECT c.collection_id, l.user_id FROM collection AS c INNER JOIN collectionLike AS l ON c.collection_id = " + input + " AND c.collection_id = l.collection_id ;") ;
        	rs4 = stmt.executeQuery("SELECT * FROM UserList WHERE User_Id = " + rs.getInt("collectionuser_id") + ";") ;
        	count = 0 ;
        	while(rs.next()) {
        		count++ ;
        	}
        	count2 = 0 ;
        	while(rs2.next()) {
        		count2++ ;
        	}
        	count3 = 0 ;
        	while(rs3.next()) {
        		count3++ ;
        	}
        	System.out.println(rs.getString("collection_name")+ " " + rs4.getInt("User_name") + " " + rs.getString("collection_description") + " movie number : " + count);
        	while(rs.next()) System.out.println(rs.getInt("movie_id") + " " + rs.getString("movie_name"));
        	System.out.println("like number : " + count3 + " comment number : " + count2);
        	System.out.println("1. movie \n 2.comment \n 3. collection edit, add \n 4. collection like\n") ;
        	input = scan.nextInt() ;
        	int userId = 19970001 ;
        	switch(input) {
        		case 1 : 
        			System.out.print("영화 리스트 중 선택 하고 싶은 번호를 고르세요 : ");
        			input=scan.nextInt() ;
        			rs=stmt.executeQuery("SELECT * FROM movie WHERE movie_id = " + input + ";") ; 
        			System.out.println(rs.getInt("movie_id") + " " 
        	       			 + rs.getString("movie_name") + " "
        	            	 + rs.getTimestamp("movie_year") + " "
        	            	 + rs.getTimestamp("movie_date") + " "
        	            	 + rs.getString("movie_country") + " "
        	            	 + rs.getInt("movie_boxOffice") + " "
        	            	 + rs.getString("movie_description") + " "
        	            	 + rs.getInt("movie_runningTime") + " "
        	            	 + rs.getString("movie_title") + " "
        	            	 + rs.getInt("movie_play") + " ") ;
        			break ;
        		case 2 :
        			System.out.println("Comment List");
        			while(rs2.next()) {
        				System.out.println(rs2.getString("User_name") + "\n" + rs2.getString("contents")) ;
        			}
        			System.out.println("comment를 추가하시겠습니까? Y/N");
        			searching = scan.next();
        			if(searching.equalsIgnoreCase("Y")) {
        				System.out.print("추가하고 싶은 comment 내용을 입력하세요 : ");
        				searching = scan.nextLine();
        				System.out.println("date 입력 ");
        				String date = scan.nextLine();
        				stmt.executeUpdate("INSERT INTO collectionComments (collection_id, collection_id, user_id, comment_date, comment_content, parent_comment_id, is_deleted) VALUES ( '" 
        																+ searching+"', "+ rs.getInt("collection_id") +", "+rs.getInt("collectionuser_id")+", '"+date + "', '" + searching + "', " + 0 + ") ;" ) ;
        			}
        		case 3 :
        			if(rs.getInt("collectionuser_id")==userId) {
        				System.out.println("1. 추가 \n 2. 수정 \n");
        				input=scan.nextInt();
        				if(input==1) {
        					String title, des, mo ;
        					System.out.println("컬렉션 추가");
        					System.out.print("컬렉션 이름 : ");
        					String name = scan.nextLine() ;
        					System.out.print("컬렉션 설명 : ");
        					des = scan.nextLine();
        					System.out.print("컬렉션 생성 날짜 : ex)2019-05-08");
        					String date = scan.nextLine();
        					System.out.print("컬렉션 무비 : ex)About Time");
        					mo = scan.nextLine();
        					stmt.executeUpdate("INSERT INTO collection(collection_name, collection_description, collection_date, collectionuser_id) "
        							+ "VALUES('" + name + "', '" + des + "', '" + date + "', " + rs.getInt("collectionuser_id") + ");") ;
        					rs2 = stmt.executeQuery("SELECT movie_id FROM movie_name WHERE = " + mo + " ;") ;
        					stmt.executeUpdate("INSERT INTO collectionMoive(collection_id, movie_id) VALUES ( " + rs.getInt("collection_id") +", " + rs2.getInt("movie_id") + ") ;") ;
        				}
        				else if(input==2) {
        					String title, des, mo ;
        					System.out.print("수정하고 싶은 collection title : \n" ) ;
        					title = scan.nextLine() ;
        					System.out.print("수정하고 싶은 Collection description : \n") ;
        					des = scan.nextLine() ;
        					System.out.print("수정하고 싶은 Collection movie : \n") ; 
        					mo = scan.nextLine() ;
        					stmt.executeUpdate("UPDATE collection SET collection_name =" + title + ", collection_description = " + des + ", WHERE collection_id = " + rs.getInt("collection_id") + ";" ) ;
        					rs = stmt.executeQuery("SELECT movie_id FROM movie WHERE movie_name =" + mo + ";") ;
        					stmt.executeUpdate("DELETE FROM collectionMovie WHERE  = " + rs.getInt("movie_id") + ";") ;
        				}
        			} 
        		case 4 :
        			if(rs3.getInt("user_id")==userId) {
        				System.out.println("좋아요를 삭제하시겠습니까? N/Y");
        				searching = scan.next();
        				if(searching.equalsIgnoreCase("Y")) {
        					stmt.executeUpdate("DELETE FROM collectionLike WHERE user_id =" + userId + ";") ;
        				}
        			}
        			else {
        				System.out.println("좋아요를 추가하시겠습니까? N/Y");
        				searching = scan.next();
        				if(searching.equalsIgnoreCase("Y")) {
        					stmt.executeUpdate("INSERT INTO collectionLike (collection_id, user_id) VALUES (" + rs3.getInt("collection_id") +", " + rs.getInt("user_id") + " ;" );
        				}
        			}
        			
        	}
        }
    }catch(ClassNotFoundException ce){
        ce.printStackTrace();            
    }catch(SQLException se){
        se.printStackTrace();    
    }catch(Exception e){
        e.printStackTrace();
    }finally{
        try{ // 연결 해제(한정돼 있으므로)
            if(rs!=null){        rs.close();            }
            if(pstmt!=null){    pstmt.close();        }
            if(stmt!=null){    stmt.close();        }
            if(conn!=null){    conn.close();        }
        }catch(SQLException se2){
            se2.printStackTrace();
            }
        }
	}
public static void moviePrint(ResultSet rs) throws SQLException {
	 
}
}
