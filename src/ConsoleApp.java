
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets; 
import javafx.scene.control.Button; 
import javafx.scene.layout.GridPane; 

import javafx.scene.control.ScrollPane;
import javafx.event.ActionEvent; 

import javafx.scene.control.TextInputDialog;
import javafx.stage.WindowEvent;



//Initiating the GUI and handling the behaviours
public class ConsoleApp extends Application {

    private static  ArrayList<Seat> BOOKED_SEAT_ARRAY  = new ArrayList<Seat>();  //This is the set of seats in this system,so this should be global
    private static final int SEATING_CAPACITY = 42; //seating capacity
    private static final String DB_FILE_NAME = "AllSystemBookings.txt"; // This file is common for all seats

    //Showing the seat details in the console
    private void showAllSeats() {
        System.out.println("This is the list of all seats:- ");
        for(Seat s:BOOKED_SEAT_ARRAY)//Iterating the booking array
            s.display();
            System.out.println("");
    }
	
    //This is used for sorting seats
    //Algorithm used is bubble sorting algorithm
	private void sortSeats()
    {
        Seat temp;
        if (BOOKED_SEAT_ARRAY.size()>1) // check if the number of orders is larger than 1
        {
            for (int x=0; x<BOOKED_SEAT_ARRAY.size()-1; x++) // bubble sort outer loop
            {
                for (int i=0; i < BOOKED_SEAT_ARRAY.size()-x-1; i++){
                    if (BOOKED_SEAT_ARRAY.get(i).compareTo(BOOKED_SEAT_ARRAY.get(i+1)) > 0)
                    {
                        temp = BOOKED_SEAT_ARRAY.get(i);
                        //BOOKED_SEAT_ARRAY.set(i,BOOKED_SEAT_ARRAY.get(i+1) );
						BOOKED_SEAT_ARRAY.set(i,BOOKED_SEAT_ARRAY.get(i+1) );
                        BOOKED_SEAT_ARRAY.set(i+1, temp);
                    }
                }
            }
        }

  }
	


    //Adding a seat to the array
    private void addSeat(int sid, String cusName){

        if (this.isSeatBooked(sid))  //This prevents Loading same seats
            return;

        Seat s1 = new Seat();
        s1.setID(sid);     //setting seatID
        s1.book(cusName);   //setting customer name
        BOOKED_SEAT_ARRAY.add(s1);

    }
    //Checking if the seat is booked or not
    private boolean isSeatBooked(int sid){
        boolean isBooked = false;
        for(Seat curSeat : BOOKED_SEAT_ARRAY ) { //searching the given sid in the array list

            if( sid == curSeat.getID()) {
                isBooked = true;
            }
        }
        return isBooked;
    }
    //displaying the empty seats in the console
    private void displayEmptySeats(){

        int index;
        System.out.println("Displaying the Empty Seats");
        for (index = 1 ;index <= SEATING_CAPACITY ; index++) {
            if(this.isSeatBooked(index) == false){
                System.out.println("seat "+index+" is available");
            }

        }



    }
    //searching a given customer from the array list using name and delete
    private void deleteCustomer(String cusName){
        boolean isCusFound = false;

        for (Iterator<Seat> iterator = BOOKED_SEAT_ARRAY.iterator(); iterator.hasNext(); ) { //iterating through the list
            Seat s = iterator.next();
            if (cusName.equals(s.getCustomer().getName())) {
                {
                    iterator.remove();
                    isCusFound = true;
                    System.out.print("Deleting customer ");
                    System.out.print(s.getCustomer().getName());
                    System.out.print(", from Seat ");
                    System.out.print(s.getID());
                }
            }
        }

        if(isCusFound == false)
            System.out.println("No customer found"); 
    }

    //Find a given customer from the array list using names
    private void findCustomerSeat(String cusName){
        boolean isCusFound = false;
        for(Seat s:BOOKED_SEAT_ARRAY) {
            if(cusName.equals(s.getCustomer().getName())){
                isCusFound = true;
                System.out.print("Customer's seat id is ");
                System.out.print(s.getID());
                System.out.println("");
            }
            if(!isCusFound)
                System.out.println(cusName + " is not found");
            
        }
    }
    //Loading the data from a file
    private void loadData() {

        File file = new File(DB_FILE_NAME);//opening the file

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String st = "";
            while ((st = br.readLine()) != null) { //read line by line
                StringTokenizer stok = new StringTokenizer(st);  //Extracting both seatID and Customer name
                int seatID = Integer.parseInt( stok.nextToken("_"));
                String cusName = stok.nextToken("_");
                addSeat(seatID, cusName);

            }
        } catch (FileNotFoundException e) {
			System.out.println("ERROR:- No File Found...");
			return;
        } catch (IOException e) {
        }
        System.out.println("Loading Data : Success");
    }
    //Clear the file content
    private void clearFile(String file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
    }
    //add given details to the file
    private void addseattofile (String line) throws IOException {


        FileWriter fw= new FileWriter(DB_FILE_NAME, true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(line);
        bw.newLine();

        bw.close();
        fw.close();
    }


    //saving all data to the file
    private void saveData(){
        System.out.println("Saving Data");
        try {
            this.clearFile(DB_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(Seat curSeat : BOOKED_SEAT_ARRAY){
            String dataLine = curSeat.getID() + "_" + curSeat.getCustomer().getName();
            try {
                System.out.println(dataLine);
                this.addseattofile(dataLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Saving Data : Success");
    }
    //Displaying the sorted seats
    private void viewSortedSeats(){
        System.out.println("Viewing seats Ordered alphabetically by name:-");
        if (BOOKED_SEAT_ARRAY.size()==0){
            System.out.println("There is nothing to sort");
            return;
        }
		this.sortSeats();
        this.showAllSeats();
        System.out.println("");
    }
    //checking whether you have entered only characters
    private boolean isValidname(String name){
        if (!name.matches("[a-zA-Z_]+")) {
            return false;
        }
        return true;
    }
    //showing the menu
    private void showMenu(){
        System.out.println("");
        System.out.println("A: Add Customer");
        System.out.println("V: View all the seats");
        System.out.println("E: Display Empty seats");
        System.out.println("D: Delete customer from seat");
        System.out.println("F: Find the seat for a given customer name");
        System.out.println("S: Store program data into file");
        System.out.println("L: Load program data from the file");
        System.out.println("O: View seats Ordered alphabetically by name");
        System.out.println("Q: Exit");

    }
	
	    public void runThis(Stage main) {

        String option = "";
            this.showMenu();
            Scanner consoleScanner =  new Scanner(System.in);
            System.out.print("Select your option:");
            option = consoleScanner.nextLine();
            option = option.toUpperCase();

            if(option.equals("A")) {
                main.show();
            }
            else if (option.equals("V")){
                this.showAllSeats();
                main.show();
            }
            else if (option.equals("E")){
                this.displayEmptySeats();
                main.show();
            }
            else if (option.equals("D")){

                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter customer name:");
                String cusName = scanner.nextLine();
                if(!isValidname(cusName)) {
                    System.out.println("Info : Invalid Name Found");
                    runThis(main); 
                }



                this.deleteCustomer(cusName);
                ScrollPane scrollPane = getUpdate(main);
                main.setScene(new Scene(scrollPane));
		    
				runThis(main); 
            }
            else if(option.equals("F")){

                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter customer name:");
                String cusName = scanner.nextLine();
                if(!isValidname(cusName)) {
                    System.out.println("Info : Invalid Name Found");
                    runThis(main); 
                }

                this.findCustomerSeat(cusName);
				runThis(main); 
            }
            else if(option.equals("O")){
                this.viewSortedSeats();
				runThis(main); 
            }
            else if(option.equals("S")){
                this.saveData();
				runThis(main); 
            }
            else if(option.equals("L")){
                this.loadData();
				runThis(main); 
            }
			else if (option.equals("Q")) {
				System.out.println("Exiting from the program");
				System.exit(0);
				return;
			}
            else{
                System.out.println("Please Enter Valid Input");
            }

    }
	
    //this is the Entry point
    public void runThis() {

        String option = "";
        do{ //This is the main loop
            this.showMenu();
            Scanner consoleScanner =  new Scanner(System.in);
            System.out.print("Enter your option: ");
            option = consoleScanner.nextLine();
            option = option.toUpperCase();

            if(option.equals("A")) {
                launch();

            }
            else if (option.equals("V")){
                this.showAllSeats();
                launch();
            }
            else if (option.equals("E")){
                this.displayEmptySeats();
                launch();
            }
            else if (option.equals("D")){

                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter customer name: ");
                String cusName = scanner.nextLine();
                if(!isValidname(cusName)) {
                    System.out.println("Info : Invalid Name Found");
                    continue;

                }

                this.deleteCustomer(cusName);
            }
            else if(option.equals("F")){

                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter customer name: ");
                String cusName = scanner.nextLine();
                if(!isValidname(cusName)) {
                    System.out.println("Info : Invalid Name Found");
                    continue;
                }


                this.findCustomerSeat(cusName);
            }
            else if(option.equals("O")){
                this.viewSortedSeats();
            }
            else if(option.equals("S")){
                this.saveData();
            }
            else if(option.equals("L")){
                this.loadData();
            }
			else if (option.equals("Q")) {
				System.out.println("Exiting from the program");
				return;
			}
            else{
                System.out.println("Please Enter Valid Input");
            }


        }while(!option.equals("Q"));

    }

    //Updating the GUI after deleting a booking
    private ScrollPane getUpdate(Stage stage)
    {
        
            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10));
            grid.setHgap(10);
            grid.setVgap(10);
    
             // create a text input dialog 
            TextInputDialog td = new TextInputDialog(""); 
    
            // setHeaderText 
            td.setHeaderText("enter your name"); 
    
    
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) //handling button events
                { 
                    System.out.println(SEATING_CAPACITY);
                    Button but = (Button)e.getSource();
                    System.out.println("handling..." + but.getText());
                    td.showAndWait(); 
    
                    // set the text of the label 
                    String cusName = td.getEditor().getText();
                    int seatID = Integer.parseInt(but.getText());
                    System.out.println(seatID); 
                    addSeat(seatID, cusName);
                    System.out.println(td.getEditor().getText()); 
                    but.setStyle("-fx-background-color: #DF013A; ");//seat was taken 
                    stage.hide();
                    runThis(stage);
                } 
            };
            //creating the button grid
            int buttonCounter = 0;
            for (int r = 0; r < 10; r++) {
                for (int c = 0; c < 10; c++) {
                    if(buttonCounter > SEATING_CAPACITY-1)
                        break;
    
                    int number =  10 * r + c;
                    Button button = new Button(String.valueOf(number+1));
                    grid.add(button, c, r);
                    int sid = number+1;
                    if (this.isSeatBooked(sid) == false) {
                        button.setStyle("-fx-background-color: #0B610B; ");
                    }
                    else {
                        button.setStyle("-fx-background-color: #DF013A; ");
                    }
                    button.setOnAction(event);
                    buttonCounter = buttonCounter + 1; 
                }
            }
    
            ScrollPane scrollPane = new ScrollPane(grid);
      
    
            return scrollPane;
    }


    @Override
    public void start(Stage stage) { //starting point of the GUI thread
        GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));
		grid.setHgap(10);
		grid.setVgap(10);

		 // create a text input dialog 
        TextInputDialog td = new TextInputDialog(""); 

        // setHeaderText 
        td.setHeaderText("enter your name"); 


		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
			public void handle(ActionEvent e) 
			{ 

				Button but = (Button)e.getSource();
				//System.out.println("handling..." + but.getText());
				td.showAndWait(); 

                // set the text of the label 
				String cusName = td.getEditor().getText();
				int seatID = Integer.parseInt(but.getText());

				addSeat(seatID, cusName);

				but.setStyle("-fx-background-color: #DF013A; ");
				stage.hide();
				runThis(stage);
			} 
		};

        int buttonCounter = 0;
		for (int r = 0; r < 10; r++) {
			for (int c = 0; c < 10; c++) {
                if(buttonCounter > SEATING_CAPACITY-1)
                    break;

				int number =  10 * r + c;
				Button button = new Button(String.valueOf(number+1));
				grid.add(button, c, r);
				int sid = number+1;
				if (this.isSeatBooked(sid) == false) { //ok, you can use it
					button.setStyle("-fx-background-color: #0B610B; ");
				}
				else { // this is taken
					button.setStyle("-fx-background-color: #DF013A; ");
				}
                button.setOnAction(event);
                buttonCounter = buttonCounter + 1; 
			}
		}

		ScrollPane scrollPane = new ScrollPane(grid);
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {  
                    @Override
                    public void handle(WindowEvent event) { //handling the close button event

                        boolean actionNeeded = false;
                        if (actionNeeded) {
                            System.exit(0);
                        } else {
                            event.consume();
							stage.hide();
							runThis(stage);
                        }

                    }
                });

		stage.setScene(new Scene(scrollPane));
		stage.show();
    }
	
	@Override
	public void stop(){
		System.out.println("Exiting from the program ...");

	}



}
