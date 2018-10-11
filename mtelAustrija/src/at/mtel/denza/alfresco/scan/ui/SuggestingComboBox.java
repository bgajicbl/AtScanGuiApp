package at.mtel.denza.alfresco.scan.ui;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

public class SuggestingComboBox extends ComboBox {

	  /**
	 * 
	 */
	private static final long serialVersionUID = -4144898152669887601L;

	public SuggestingComboBox(String caption, String captionPropertyId) {
	    super(caption);
	    // the item caption mode has to be PROPERTY for the filtering to work
	    setItemCaptionMode(ItemCaptionMode.PROPERTY);

	    // define the property name of the CountryBean to use as item caption
	    setItemCaptionPropertyId(captionPropertyId);
        
	    setImmediate(true);
	    setNullSelectionAllowed(false);
		setScrollToSelectedItem(true);
	  }

	  public SuggestingComboBox() {
	    this(null, "id");
	  }

	  /**
	   * Overwrite the protected method
	   * {@link ComboBox#buildFilter(String, FilteringMode)} to return a custom
	   * {@link SuggestionFilter} which is only needed to pass the given
	   * filterString on to the {@link CustomerSuggestingContainer}.
	   */
	  @Override
	  protected Filter buildFilter(String filterString, FilteringMode filteringMode) {
	    return new CustomerSuggestingContainer.SuggestionFilter(filterString);
	  }
	}
