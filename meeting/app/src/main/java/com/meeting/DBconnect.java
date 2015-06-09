package com.meeting;

import android.util.Log;
import android.widget.Toast;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;


public class DBconnect {
    Connection con = null;
    PreparedStatement stat = null, stat1 = null;
    ResultSet rs = null, rp = null;
    String sql, sql1;
    String userid;

    public DBconnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/meeting", "root", "");
            if (!con.isClosed()) {
                Log.e("error", "error");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    List<String> findmeetingid()
    {
        List<String> list = new ArrayList<String>();
        String sql = "select distinct roomid from roomlog";
        try {
            stat = con.prepareStatement(sql);
            rs = stat.executeQuery();
            if (rs.next())
            {
                list.add(rs.getString("roomid").trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


	
	int isExist(String userid,String pass){
		
		String sql="select * from userlog where userid=? ";
		int flag=0;
		try{
			stat=con.prepareStatement(sql);
			((PreparedStatement) stat).setString(1,userid);
			rs=stat.executeQuery();
			if(rs.next()){
				String password=rs.getString("password").trim();
				//
				String right=rs.getString("right").trim();
				if(password.equals(pass))
				{
					if(rs.getInt(3)==0)
					     flag=1;
					if(rs.getInt(3)==1)
				         flag=2;
				}
				else
				{
					flag=-1;	
				}
			}
		   else
				{
				  flag=0;
				}
		}catch(SQLException e1){
			e1.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e2){
				e2.printStackTrace();
		     }
			
		}
		return flag;
	}

//	String selectInfo(String id,String name)
//	{
//		String result="";
//		try{
//			if(name.equals(""))
//			{
//				sql="select * from bookinfo where code=? ";
//				stat=con.prepareStatement(sql);
//				stat.setString(1,id);
//			}
//			else if(id.equals(""))
//			{
//				sql="select * from bookinfo where bookname=? ";
//				stat=con.prepareStatement(sql);
//				stat.setString(1,name);
//			}
//			else
//			{
//				sql="select * from bookinfo where code=? and bookname=? ";
//				stat=con.prepareStatement(sql);
//				stat.setString(1,id);
//				stat.setString(2,name);
//			}
//			rs=stat.executeQuery();
//			if(rs.next())
//			{
//				do
//				{
//					result=result+rs.getString(1)+"\t";
//					result=result+rs.getString(2)+"\t";
//					result=result+rs.getString(3)+"\t";
//					result=result+rs.getString(4)+"\t";
//					result=result+rs.getString(5)+"\t";
//					result=result+rs.getString(6)+"\t";
//					result=result+rs.getString(7)+"\t";
//					result=result+rs.getString(8)+"\n";
//			    }while(rs.next());
//		   }
//			else
//			{
//				result="û�и�ͼ�����Ϣ��";
//			}
//	   }
//		catch(Exception e1)
//		{
//			e1.printStackTrace();
//		}
//		finally
//		{
//			try
//			{
//				con.close();
//			}
//		    catch(SQLException e)
//		    {
//			    e.printStackTrace();
//		    }
//		}
//       return result;
//	}
//
//	String selectuserInfo(String userid)
//	{
//		String result="";
//			try{
//				sql="select * from userinfo where userid=? ";
//				stat=con.prepareStatement(sql);
//				stat.setString(1,userid);
//				rs=stat.executeQuery();
//				if(rs.next())
//				{
//		          do
//				   {
//					result=result+rs.getString(1)+"\t";
//					result=result+rs.getString(2)+"\t";
//					result=result+rs.getString(3)+"\t";
//					result=result+rs.getString(4)+"\t";
//					result=result+rs.getString(5)+"\n";
//			       }while(rs.next());
//				}
//				else
//				{
//					result="û�и��û�����Ϣ��";
//				}
//			}
//		catch(Exception e1)
//		{
//			e1.printStackTrace();
//		}
//		finally
//		{
//			try
//			{
//				con.close();
//			}
//		    catch(SQLException e)
//		    {
//			    e.printStackTrace();
//		    }
//		}
//       return result;
//	}
//	int selectuserInfo1(String userid)
//	{
//		int result=0;
//		String resul="";
//			try{
//				//sql="select borrowed from userinfo where userid=? ";
//				sql="select borrowed from userinfo where userid=? ";
//				stat=con.prepareStatement(sql);
//				stat.setString(1,userid);
//				rs=stat.executeQuery();
//				 while(rs.next())
//				   {
//				       resul=rs.getString("borrowed");
//				       result=Integer.parseInt(resul);
//				   }
//			    }
//			catch(Exception e1)
//			{
//				e1.printStackTrace();
//			}
//			finally
//			{
//			}
//			return result;
//	}
//
//	int selectreader(String code)
//	{
//		int r=0;
//		String result="";
//			try{
//				sql="select code from readerbook where code=? ";
//				stat=con.prepareStatement(sql);
//				stat.setString(1,code);
//				rs=stat.executeQuery();
//				if(rs.next())
//				{
//					r=1;
//		          do
//				   {
//					result=rs.getString("code");
//			       }while(rs.next());
//				}
//
//			}
//			catch(Exception e1)
//			{
//				e1.printStackTrace();
//			}
//			finally
//			{
//			}
//       return r;
//	}
//
//
//	String selectborrowall()//��ʾ���н�����Ϣ
//	{
//		String result="";
//			try{
//				//sql="select * from readerbook  ";
//				//stat=con.prepareStatement(sql);
//				//rs=stat.executeQuery();
//				java.sql.Statement st;
//				st=con.createStatement();
//				sql="select * from readerbook  ";
//
//				rs=st.executeQuery(sql);
//
//				if(rs.next())
//				{
//		          do
//				   {
//					result=result+rs.getString(1)+"\t";
//					result=result+rs.getString(2)+"\t";
//					result=result+rs.getString(3)+"\t";
//					//result=result+rp.getString(4)+"\n";
//			       }while(rs.next());
//				}
//
//			}
//			catch(Exception e1)
//			{
//				e1.printStackTrace();
//			}
//			finally
//			{
//			}
//       return result;
//	}
//
//	int deleteInfo(String id)
//	{
//		int n=0;
//
//		String sql1="delete from readerbook where code='"+id+"'";
//		String sql2="delete from bookinfo where code='"+id+"'";
//		java.sql.Statement st;
//		try
//		{
//			st=con.createStatement();
//
//			con.setAutoCommit(false);
//			st.executeUpdate(sql1);
//
//			st.executeUpdate(sql2);
//			con.commit();
//			con.setAutoCommit(true);
//			n=1;
//		}
//		catch(SQLException e)
//		{
//			try
//			{
//				con.rollback();
//			}
//			catch(SQLException e1)
//			{
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//		finally
//		{
//			try
//			{
//				con.close();
//			}
//			catch(SQLException e)
//			{
//				e.printStackTrace();
//			}
//		}
//		return n;
//	}
//
//	int deleteuserInfo(String id)
//	{
//		int n=0;
//
//		String sql1="delete from readerbook where userid='"+id+"'";
//		String sql2="delete from userinfo where userid='"+id+"'";
//		String sql3="delete from userlog where userid='"+id+"'";
//		java.sql.Statement st;
//		try
//		{
//			st=con.createStatement();
//
//			con.setAutoCommit(false);
//			st.executeUpdate(sql1);
//			st.executeUpdate(sql2);
//			st.executeUpdate(sql3);
//			con.commit();
//			con.setAutoCommit(true);
//			n=1;
//		}
//		catch(SQLException e)
//		{
//			try
//			{
//				con.rollback();
//			}
//			catch(SQLException e1)
//			{
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//		finally
//		{
//			try
//			{
//				con.close();
//			}
//			catch(SQLException e)
//			{
//				e.printStackTrace();
//			}
//		}
//		return n;
//	}
//
	int  insertInfo(String code,String aclass,String name,String writer,String publish,String date, String price,int num)
	{
		int n=0;
		//String sql="insert into bookinfo (code,classify,bookname,writer,publish,publishdata, price,stock) values("+code+","+aclass+","+name+","+writer+","+publish+","+date+","+price+","+num+")";
		java.sql.Statement st;
		String sql="insert into bookinfo values('"+code+"','"+aclass+"','"+name+"','"+writer+"','"+publish+"','"+date+"','"+price+"',"+num+")";//���͵Ĵ�ֵʹ������
		try
		{
			st=con.createStatement();
			con.setAutoCommit(false);
			st.executeUpdate(sql);
			con.commit();
			con.setAutoCommit(true);
			n=1;
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return n;
	}
	
	
	int  insertuserInfo(String id,String name,String tel,int borrowed,int isborrow,String pass,int right)
	{
		int n=0;
		//String sql="insert into bookinfo (code,classify,bookname,writer,publish,publishdata, price,stock) values("+code+","+aclass+","+name+","+writer+","+publish+","+date+","+price+","+nu+")";
		java.sql.Statement st;
		String sql1="insert into userinfo values('"+id+"','"+name+"','"+tel+"',"+borrowed+","+isborrow+")";
		String sql2="insert into userlog values('"+id+"','"+pass+"',"+right+")";
		try
		{
			st=con.createStatement();
			con.setAutoCommit(false);
			st.executeUpdate(sql1);
			st.executeUpdate(sql2);
			con.commit();
			con.setAutoCommit(true);
			n=1;
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return n;
	}
	int  modify1(String code,String aclass)
	{
		int n=0;
		
		java.sql.Statement st;
		String sql="update  bookinfo set classify='"+aclass+"' where code='"+code+"'";
		try
		{
			st=con.createStatement();
			con.setAutoCommit(false);
			st.executeUpdate(sql);
			con.commit();
			con.setAutoCommit(true);
			n=1;
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return n;
	}
	
	int  modify2(String code,String name)
	{
		int n=0;
		
		java.sql.Statement st;
		String sql="update  bookinfo set name='"+name+"' where code='"+code+"'";
		try
		{
			st=con.createStatement();
			con.setAutoCommit(false);
			st.executeUpdate(sql);
			con.commit();
			con.setAutoCommit(true);
			n=1;
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return n;
	}
	
	int  modify3(String code,String writer)
	{
		int n=0;
		
		java.sql.Statement st;
		String sql="update  bookinfo set writer='"+writer+"' where code='"+code+"'";
		try
		{
			st=con.createStatement();
			con.setAutoCommit(false);
			st.executeUpdate(sql);
			con.commit();
			con.setAutoCommit(true);
			n=1;
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return n;
	}
	
	int  modify4(String code,String publish)
	{
		int n=0;
		
		java.sql.Statement st;
		String sql="update  bookinfo set publish='"+publish+"' where code='"+code+"'";
		try
		{
			st=con.createStatement();
			con.setAutoCommit(false);
			st.executeUpdate(sql);
			con.commit();
			con.setAutoCommit(true);
			n=1;
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return n;
	}
	
	int  modify5(String code,String pulishdate)
	{
		int n=0;
		
		java.sql.Statement st;
		String sql="update  bookinfo set pulishdate='"+pulishdate+"' where code='"+code+"'";
		try
		{
			st=con.createStatement();
			con.setAutoCommit(false);
			st.executeUpdate(sql);
			con.commit();
			con.setAutoCommit(true);
			n=1;
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return n;
	}
	int  modify6(String code,String price)
	{
		int n=0;
		java.sql.Statement st;
		String sql="update  bookinfo set price='"+price+"' where code='"+code+"'";
		try
		{
			st=con.createStatement();
			con.setAutoCommit(false);
			st.executeUpdate(sql);
			con.commit();
			con.setAutoCommit(true);
			n=1;
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return n;
	}
	int  modify7(String code,int stock)
	{
		int n=0;
		
		java.sql.Statement st;
		String sql="update  bookinfo set stock="+stock+" where code='"+code+"'";
		try
		{
			st=con.createStatement();
			con.setAutoCommit(false);
			st.executeUpdate(sql);
			con.commit();
			con.setAutoCommit(true);
			n=1;
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return n;
	}
	//�洢��
/*	String borrow(String userid,String bookid)
	{
		String result="";
		try{
			
			   CallableStatement call = (CallableStatement) con.prepareCall("{call BorrowBook(?,?)}");
			   call.setString(1, "userid");
			   call.setString(2, "bookid");
			   call.execute();
			    sql="select * from readerbook where userid=? and code=? ";
				stat=con.prepareStatement(sql);
				stat.setString(1,userid);
				stat.setString(2,bookid);
			    rs=stat.executeQuery();
			  if(rs.next())
				{
					do
					{
						result=result+rs.getString(1)+"\t";
						result=result+rs.getString(2)+"\t";
						result=result+rs.getString(3)+"\t";
						result=result+rs.getString(4)+"\t";
				    }while(rs.next());
			   }
				
	   }
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
		    catch(SQLException e)
		    {
			    e.printStackTrace();
		    }
		}
       return result;		
	}
	*/
	int selectreader(String uid,String bid)
	{
		int n=0;
		try{
			   String sql="select * from readerbook where userid=? and code=? ";
				stat=con.prepareStatement(sql);
				stat.setString(1,uid);
				stat.setString(2,bid);
			    rs=stat.executeQuery();
			  if(rs.next())
			  {
				n=1;
			  }		
	       }
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		finally
		{	
		}
       return n;		
	}
	
	int selectreturn(String uid,String bid)
	{
		int n=0;
		try{
			   String sql="select * from returnbook where userid=? and code=? ";
				stat=con.prepareStatement(sql);
				stat.setString(1,uid);
				stat.setString(2,bid);
			    rs=stat.executeQuery();
			  if(rs.next())
			  {
				n=1;
			  }		
	       }
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		finally
		{	
		}
       return n;		
	}
	
	int selectoverday(String uid)
	{
		int n=0;
		long day=0;
		String result;
		try{
			   String sql="select borrowdate from readerbook where userid=? ";
				stat=con.prepareStatement(sql);
				stat.setString(1,uid);
			    rs=stat.executeQuery();
			    while(rs.next())
				   {
				       result=rs.getString("borrowdate");
				       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				       java.util.Date rDate = sdf.parse(result);
				       java.util.Date cDate = new java.util.Date();
				       day = (cDate.getTime() - rDate.getTime())/(24*60*60*1000);
				       if(day>20)
				       {	
				    	   n=1;
				    	   break;
				       }
				   }
			   
	       }
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		finally
		{	
		}
       return n;		
	}
	
	
	String borrow(String uid,String bid)
	{
		String result="";
		try{
			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf.format(new java.util.Date());
				String  sql1="insert into readerbook (id,userid,`code`,borrowdate,`day`)values (null,'"+bid+"','"+uid+"','"+date+"',30)";
				String  sql2="update bookinfo  set stock=stock-1 where code='"+uid+"'";
				String  sql3="update userinfo  set borrowed=borrowed+1   where userid='"+bid+"'";
				String sql5="delete from returnbook where code='"+uid+"'";
				java.sql.Statement st;
				st=con.createStatement();
				st.execute(sql1);				
				st.executeUpdate(sql2);
				st.executeUpdate(sql3);
				st.executeUpdate(sql5);
				 String sql4="select * from readerbook where userid=? and code=? ";
					stat=con.prepareStatement(sql4);
					stat.setString(1,bid);
					stat.setString(2,uid);
				    rs=stat.executeQuery();
				  if(rs.next())
					{
						do
						{
							result=result+rs.getString(1)+"\t";
							result=result+rs.getString(2)+"\t";
							result=result+rs.getString(3)+"\t";
							result=result+rs.getString(4)+"\t";
							result=result+rs.getString(5)+"\t";
					    }while(rs.next());
				   }		
	   }
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
		    catch(SQLException e)
		    {
			    e.printStackTrace();
		    }
		}
       return result;		
	}
	
	String back(String uid,String bid)
	{
		String result="";
		try{
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf.format(new java.util.Date());
				String  sql1="insert into returnbook (id,userid,`code`,returndate)values (null,'"+bid+"','"+uid+"','"+date+"')";
				String  sql2="update bookinfo  set stock=stock+1 where code='"+uid+"'";
				String  sql3="update userinfo  set borrowed=borrowed-1   where userid='"+bid+"'";
				String sql5="delete from readerbook where code='"+uid+"'";
				java.sql.Statement st;
				st=con.createStatement();
				st.execute(sql1);	
				st.executeUpdate(sql2);
				st.executeUpdate(sql3);
				st.executeUpdate(sql5);
				 String sql4="select * from returnbook where userid=? and code=? ";
					stat=con.prepareStatement(sql4);
					stat.setString(1,bid);
					stat.setString(2,uid);
				    rs=stat.executeQuery();
				  if(rs.next())
					{
						do
						{
							result=result+rs.getString(1)+"\t";
							result=result+rs.getString(2)+"\t";
							result=result+rs.getString(3)+"\t";
							result=result+rs.getString(4)+"\t";
					    }while(rs.next());
				   }		
	   }
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
		    catch(SQLException e)
		    {
			    e.printStackTrace();
		    }
		}
       return result;		
	}
	
}
