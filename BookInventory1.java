import java.io.*;
import java.util.Scanner;

public class BookInventory1{

	public static void main(String[] args){

		//number of books in input file
		int nBooks;
		Scanner kb = new Scanner(System.in);

		//name of the corrected ISBN catalog file to be created
		String outputFileName = null;
		
		//Ask user to name the output file that contains
		//the corrected ISBN numbers
		System.out.println("Please enter the name of the corrected catalog file:");
		outputFileName = kb.next();
		fixInventory("Initial_Book_Info.txt",outputFileName);

		//TO DO: check for existing files of that name. In case
		//user gives the name of a preexisting file, reject input,
		//display file susize and repeat query



		

		//TO DO: a whole lot more…


	}
	//fixInventory populates bkArray with objects created using 
	//the input file, checks for duplicate instances of an ISBN
	//number, and then writes the contents of the array to an
	//output file
	public static void fixInventory(String inputFileName, String outputFileName){

		Scanner input = new Scanner(System.in);
		String whichBook = null;
		String line = null;

		Book[] bkArray = new Book[numberOfLines(inputFileName)];
		
		//Begin file input with FileReader object
		
		Scanner inputStream = null;
		try{
			inputStream = new Scanner(new FileInputStream(inputFileName));
		}
		catch(FileNotFoundException e){
			System.out.println("File "+inputFileName+" was not found");
			System.out.println("or could not be opened.");
			System.exit(0);
		}
		for(int i=0; i < bkArray.length; i++){
			bkArray[i] = new Book(inputStream.nextLong(),inputStream.next(),inputStream.nextInt(),inputStream.next(),inputStream.nextDouble(),inputStream.nextInt());
			inputStream.nextLine();
		}
		System.out.println("This is the array of books: ");
		for(Book i : bkArray){
			System.out.println(i);
		}
    	//enhanced for loop iterates over all Book objects in the
    	//array

    	for(Book book : bkArray){
    		
    		//for a given book, we must go through the whole array searching for
    		//duplicate ISBNs

    		//as we find more books with  the same array, we add these to an
    		//auxiliary aray
    		Book[] dupISBNbooksAlpha = new Book[bkArray.length];

    		//in order to trigger the input of the correct ISBNs we need a variable
    		//that tells us whether a duplicate ISBN was found
    		boolean isDuplicate = false;

    		//the following is a temporary variable that stores the ISBN that currently
    		//appears on multiple books
    		long duplicateISBN = 0;

    		//the following is a counter that adds up the number of books with the same
    		//given ISBN
    		int numberOfDuplicates = 0;
    		
    		//a second enhanced for loop cycles through the book array, comparing
    		//the current book with all others
    		for(Book comparedBook : bkArray){
    		
    			if(book.getIsbn() == comparedBook.getIsbn() && book.getTitle() != comparedBook.getTitle()){
    				isDuplicate = true;
    				duplicateISBN = book.getIsbn();

    				//the following line populates the temporary array with all the
    				//books that currently have the ISBN stored in in the variable
    				//duplicateISBN
    				dupISBNbooksAlpha[numberOfDuplicates] = comparedBook;
    				numberOfDuplicates++;

    			}
    		}
    		Book[] dupISBNbooks = new Book[numberOfDuplicates];
    		for(int i=0; i < numberOfDuplicates;i++){
    			dupISBNbooks[i] = dupISBNbooksAlpha[i];
    		}
    		
    		// System.out.println("These are the books with a repeated ISBN: ");
    		// for(Book i : dupISBNbooks){
    		// 	System.out.println(i);
    		// }

    		//if a duplicate ISBN was found, we begin the process of correcting the ISBNs
    		if(isDuplicate == true){
    			System.out.println("\nThe ISBN "+duplicateISBN+" was found more than once for the following books: ");
    			
    			//Display which books have the same current ISBN
    			for(Book i : dupISBNbooks){
    				System.out.println(i.toString());
    			}
    			System.out.println();

    			//prompt the user to enter the right ISBN while making sure the entry isn't a
    			//duplicate ISBN using the method checkDuplicateISBN()
    			for(Book dupBook : dupISBNbooks){
    				Scanner kb = new Scanner(System.in);

    				boolean done = false;
    				while(!done){
    					try{
	    					System.out.println("Please enter the correct ISBN for "+dupBook.getTitle());
	    					dupBook.setIsbn(kb.nextLong());
	    					
	    					if(checkDuplicateISBN(dupBook, bkArray) == false){
	    						done = true;
	    					}
	    				}
	    				catch(DuplicateISBNException sadPotato){
	    					String message = sadPotato.getMessage();
	    					System.out.println(message);
	    				}
    								    
				    }		
    			}

    		}
    	}
    	PrintWriter outputStream = null;
			try{
				outputStream = new PrintWriter(new FileOutputStream("/home/feasinde/Documentos/COMP249_assignment_2/"+outputFileName));

			}
		catch(FileNotFoundException e){
			System.out.println("Error opening the file stuff.txt.");
			System.exit(0);
		}
		for(Book book : bkArray){
			outputStream.println(book.toString());
		}
		outputStream.close();
	    
	}

	//checkDuplicateISBN takes a Book object and an array of Book objects and returns false
	//if the ISBN of the object is not found in the ISBNs of the array. 
	//Otherwise, it throws an exception
	private static boolean checkDuplicateISBN(Book book, Book[] bkArray) throws DuplicateISBNException{
		for(Book arrayBook : bkArray){
			if(book.getIsbn() == arrayBook.getIsbn() && book.getTitle() != arrayBook.getTitle()){
				throw new DuplicateISBNException("That ISBN belongs to the book: "+arrayBook.getTitle());
			}
		}
		return false;
	}

	//numberOfLines receives a text file location
	//and returns how many lines the input file has
	private static int numberOfLines(String fileLocation){
		int lines = 0;
		try{
				//Begin file input with FileReader object
				FileReader fileReader = new FileReader(fileLocation);

				//Wrap fileReader with BufferedReader so we can read the file
				//line by line
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				
				//readLine() is the method that reads the file one line at the time
				while (bufferedReader.readLine() != null){lines++;}
				bufferedReader.close();
		}
		catch(IOException ex) {

	        System.out.println(
	            "Error reading file '" 
	            + fileLocation + "'");
	    }
	    return lines;		

		
		
	}
}