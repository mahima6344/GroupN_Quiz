package com.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class Quiz {
	public void getQuestions() {
		String ans, tempRes, studentName;
		int score = 0;
		HashMap<String, String> hm = new HashMap<>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz", "root", "root");
			// Order by rand() method display question in random order
			PreparedStatement ps = con.prepareStatement("select * from questions ORDER BY RAND();");
			// Statement stmt = con.createStatement();
			// all quetions with answer and option are store in rs
			ResultSet rs = ps.executeQuery();

			System.out.println("----- Test Will Start After You Enter Your Name ----");
			System.out.println("----- You Have To Attend 10 Questions ----");
			// get student information
			System.out.println("Enter your full name");
			Scanner s = new Scanner(System.in);
			studentName = s.nextLine();

			System.out.println("----- Test Started ----");
			// to read each value of rs we have use while loop
			while (rs.next()) {
				// to compare ans we use hashmap with a,b,c,d keys
				hm.put("A", rs.getString("option1"));
				hm.put("B", rs.getString("option2"));
				hm.put("C", rs.getString("option3"));
				hm.put("D", rs.getString("option4"));
				// stored original answer from rs into ans variable
				ans = rs.getString("answer");
				// Display values
				System.out.println(rs.getInt("id") + " " + rs.getString("question"));
				// print data from hashmap using forloop
				for (String key : hm.keySet()) {
					System.out.println(key + "  " + hm.get(key));
				}

				System.out.println("Enter your answer");
				// read answer from user
				Scanner scanner = new Scanner(System.in);
				tempRes = scanner.nextLine();
				// using user answer(key) i.e a,b,c,d we fetch value from hashmap
				String val = (String) hm.get(tempRes);
				// check user answer and original answer are same
				if (ans.equals(val)) {
					System.out.println("Correct");
					score++;
				} else {
					System.out.println("Wrong");
				}
			}
			rs.close();
			ps.close();
			con.close();
			if (score <= 10 && score >= 8) {
				System.out.println("You got grade CLASS A");
			} else if (score <= 8 && score >= 6) {
				System.out.println("You got grade CLASS B");
			} else if (score == 5) {
				System.out.println("You got grade CLASS C");
			} else {
				System.out.println("You Got FAILED");
			}
			insertStudentInfo(studentName, score);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void insertStudentInfo(String studentName, int score) throws SQLException, ClassNotFoundException {
		// try-with-resource statement will auto close the connection.
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz", "root", "root");
			PreparedStatement ps = con.prepareStatement(" INSERT INTO student  (name, score) VALUES (?,?) ");

			ps.setString(1, studentName);
			ps.setInt(2, score);
			System.out.println("You Got Score: " + score);
			ps.executeUpdate();
		} catch (ClassNotFoundException e) {
			System.out.println(e);
			;
		}
	}

	public void getAllStudentInfo() throws SQLException, ClassNotFoundException {
		// try-with-resource statement will auto close the connection.
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz", "root", "root");
			PreparedStatement ps = con.prepareStatement("select * from student ORDER BY  score ASC");
			// Statement stmt = con.createStatement();
			// all quetions with answer and option are store in rs
			ResultSet rs = ps.executeQuery();
			System.out.println("STUDENT NAME" + "       " + "SCORE");
			while (rs.next()) {

				System.out.println(rs.getString("name") + "       " + rs.getString("score"));
			}
		} catch (ClassNotFoundException e) {
			System.out.println(e);
			;
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Quiz quiz = new Quiz();
		int choice;
		while (true) {
			System.out.println("Select any choice number from below \n");
			Scanner scanner = new Scanner(System.in);
			System.out.println("\n1.Start Quiz\n");
			System.out.println("2.Get All Student Result\n");
			System.out.println("3. Quit\n");
			System.out.println("\nEnter your choice : ");
			choice = scanner.nextInt();
			switch (choice) {
			case 1:
				quiz.getQuestions();
				break;
			case 2:
				quiz.getAllStudentInfo();
				break;
			case 3:
				System.exit(0);
			default:
				System.out.println("\nWrong choice\n");
			}/* End of switch */
		}

	}

}
