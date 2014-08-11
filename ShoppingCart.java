public class ShoppingCart {

	public ShoppingCart(int movieID, String movieTitle, int qnt) {
		title = movieTitle;
		movie_id = movieID;
		quantity = qnt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String titleName) {
		title = titleName;
	}

	public int getMovieID() {
		return movie_id;
	}

	public void setMovieID(int movieID) {
		movie_id = movieID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantityNum) {
		quantity = quantityNum;
	}

	public void incrementQuantity() {
		quantity++;
	}

	public void decrementQuantity() {
		quantity--;
	}

	public void checkCart() {

	}

	private String title;
	private int movie_id;
	private int quantity;
}
