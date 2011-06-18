package life;

public class Life {
	private static Model model;
	
	public static void main(String[] args) {
		final int rows;
		if (args.length > 0){
			try {
				rows = Integer.parseInt(args[0]);
				
				if (rows < 4){
					System.err.println("The number of rows should be greater than 4");
					System.exit(1);
				}
				
				model = new Model(rows);
				
			} catch (NumberFormatException e) {
				System.err.println("The first parameter should be a number");
				System.exit(1);
			}
		}
		else {
			model = new Model();
		}
		
		new View(model);
	}
}
