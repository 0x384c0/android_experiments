package com.desu.experiments.view.adapter.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desu.experiments.R;

/**
 * Simple text item. Just have text.
 */
public class ListViewCardItem extends ListViewBaseItem {

	// Text for the item
	private String title;
	private String subTitle;

	public ListViewCardItem(String title,String subtitle) {
		super();
		setTitle(title);
		setSubTitle(subtitle);
		setLayoutId(R.layout.item_card);
	}

	/**
	 * Setter for the item text.
	 * 
	 * @param title
	 *            String
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * Getter for the item text.
	 * 
	 * @return String text
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Final implementation of the getView method. I just calls super to prepare
	 * view and then fills it with appropriate data.
	 */
	@Override
	public View getView(LayoutInflater inflater, View convertView,
			ViewGroup root) {
		// Call super class to give us usable view
		convertView = super.getView(inflater, convertView, root);

		// Now we can fill in the layout data
		((TextView) convertView.findViewById(R.id.item_title)).setText(getTitle());
		((TextView) convertView.findViewById(R.id.item_subtitle)).setText(getSubTitle());

		return convertView;
	}

}
