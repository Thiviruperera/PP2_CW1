
//Seat details are here
public class Seat {

    private int id;
    private Customer cus;
    private boolean isAvailable;


    public Customer getCustomer() {
        return this.cus;
    }

    //display seat object detail
    public void display() {
		System.out.println("=======");
        System.out.println(this.id);
        System.out.println(this.cus.getName());
		System.out.println("=======");
    }
    public boolean isAvailable(){
        return this.isAvailable;
    }
    public void cancel(){
        System.out.println("cancelled");
        this.isAvailable = true;
    }

    public void book(String cusName){
        cus = new Customer();
        cus.setName(cusName);
        //System.out.println("Booked");
        this.isAvailable = false;
    }

    public void setID(int x) {
        this.id = x;
    }

    public int getID() {
        return this.id;
    }

    //comparing to seat objects
	public int compareTo(Seat z) 
	{
      int res=0;
        if (this.cus.getName().compareTo(z.cus.getName()) < 0) {
			res=-1;  
		}
        if (this.cus.getName().compareTo(z.cus.getName()) > 0){
			res=1;
		}
      return res;
	}


}
