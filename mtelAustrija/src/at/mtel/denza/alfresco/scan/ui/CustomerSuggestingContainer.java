package at.mtel.denza.alfresco.scan.ui;

import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.UnsupportedFilterException;

import at.mtel.denza.alfresco.jpa.Customer;
import at.mtel.denza.alfresco.scan.pojo.ListUtil;

/**
 * This is a specialized {@link BeanItemContainer} which redefines the filtering
 * functionality by overwriting method
 * {@link com.vaadin.data.util.AbstractInMemoryContainer#addFilter(Filter)}.
 * This method is called internally by the filtering code of a ComboBox.
 */
public class CustomerSuggestingContainer extends BeanItemContainer<Customer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Customer defaultCustomer;

	public CustomerSuggestingContainer() throws IllegalArgumentException {
		super(Customer.class);
	}

	public CustomerSuggestingContainer(Customer defaultCustomer) throws IllegalArgumentException {
		this();
		addBean(defaultCustomer);
		this.defaultCustomer = defaultCustomer;
	}

	/**
	 * This method will be called by ComboBox each time the user has entered a new
	 * value into the text field of the ComboBox. For our custom ComboBox class
	 * {@link SuggestingComboBox} it is assured by
	 * {@link SuggestingComboBox#buildFilter(String, com.vaadin.shared.ui.combobox.FilteringMode)}
	 * that only instances of {@link SuggestionFilter} are passed into this method.
	 * We can therefore safely cast the filter to this class. Then we simply get the
	 * filterString from this filter and call the database service with this
	 * filterString. The database then returns a list of country objects whose
	 * country names begin with the filterString. After having removed all existing
	 * items from the container we add the new list of freshly filtered country
	 * objects.
	 */
	@Override
	protected void addFilter(Filter filter) throws UnsupportedFilterException {
		SuggestionFilter suggestionFilter = (SuggestionFilter) filter;
		filterItems(suggestionFilter.getFilterString());
	}

	private void filterItems(String filterString) {
		if (filterString != null && filterString.length() < 4) {
			return;
		}

		removeAllItems();
		// call web service
		List<Customer> countries = ListUtil.genericGetFromWebService("customers", new Customer());
		addAll(countries);
	}

	/**
	 * This method makes sure that the selected value is the only value shown in the
	 * dropdown list of the ComboBox when this is explicitly opened with the arrow
	 * icon. If such a method is omitted, the dropdown list will contain the most
	 * recently suggested items.
	 */
	public void setSelectedCustomer(Customer customer) {
		removeAllItems();
		addBean(customer);
	}

	/**
	 * The sole purpose of this {@link Filter} implementation is to transport the
	 * current filterString (which is a private property of ComboBox) to our custom
	 * container implementation {@link CustomerSuggestingContainer}. Our container needs
	 * that filterString in order to fetch a filtered country list from the
	 * database.
	 */
	public static class SuggestionFilter implements Container.Filter {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String filterString;

		public SuggestionFilter(String filterString) {
			this.filterString = filterString;
		}

		public String getFilterString() {
			return filterString;
		}

		@Override
		public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
			// will never be used and can hence always return false
			return false;
		}

		@Override
		public boolean appliesToProperty(Object propertyId) {
			// will never be used and can hence always return false
			return false;
		}
	}
}