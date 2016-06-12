package manipulate;

public interface Pagination {

	/*
	 * return collection 
	 * put one collection you want to play per page
	 * put number per page
	 * put number start of record 
	 * put number end of record 
	 */
	public void first();
	public void last();
	public void next();
	public void previous();
	public void showList();
	public void goTo();

	
	/**
	 * @Method setNumRow
	 * @Param Number of Row
	 * 
	 */
	public void setNumRow(int numberRow);
	
	/**
	 * @Method getNumRow
	 * @return Number of Row
	 */
	public int getNumRow();
	/**
	 * @Method setRowPosition
	 * @param positionNumber
	 */
	
	public void setPagePosition(int positionNumber);
	/**
	 * @method getrowPosition // return position of row
	 * @return rowPosition
	 */
	public int getPagePosition();
	
	
}
