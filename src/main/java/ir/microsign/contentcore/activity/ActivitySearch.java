package ir.microsign.contentcore.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.microsign.R;
import ir.microsign.contentcore.adapter.SuggestionAdapter;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.object.Category;
import ir.microsign.contentcore.object.Content;
import ir.microsign.contentcore.object.Suggest;
import ir.microsign.contentcore.view.RichWebView;
import ir.microsign.contentcore.view.SearchOptionsView;
import ir.microsign.dialog.MessageDialog;
import ir.microsign.dialog.WaitingDialog;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.view.ViewHtml;

/**
 * Created by Mohammad on 17/12/2014.
 */
public class ActivitySearch extends Activity implements View.OnClickListener {
    public static List<?> mItems = null;
//    public DataSource mDataSource = null;
    public DrawerLayout mDrawerLayout = null;
    public ImageView mImgSearch = null;
    public AutoCompleteTextView mEtxtSearch = null;
    public SearchOptionsView mSearchOptions = null;
//    ParallaxListView mList = null;
public String mCurrentCategory = "";
    public String mRootTag = null;
    public int mOffset = 0, mLimit = 4;
    //	void showFullContent(int contentId) {
//		ArrayList<Integer> list=new ArrayList<Integer>();
//		for (Object content:mItems)
//		list.add(((Content)content)._id);
////	ActivityFullContent.show(this, contentId, list);
//
//	}
    public  Category mCategory = null;
    public  String mTextValue = null;
    public  RichWebView mRichWebView = null;
    public  InputMethodManager imm = null;

    public static void show(Context context, String rootCatTag, String catId) {
        Intent intent = new Intent(context, ActivitySearch.class);
        intent.putExtra("current_category", catId);
        intent.putExtra("root_tag", rootCatTag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLimit = getResources().getInteger(R.integer.search_limit);
        mCurrentCategory = getIntent().getStringExtra("current_category");
        mRootTag = getIntent().getStringExtra("root_tag");
        setContentView(R.layout.layout_search);
        findViewById(R.id.img_header_setting2).setOnClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSearchOptions = (SearchOptionsView) findViewById(R.id.search_options);

        findViewById(R.id.img_header_back).setOnClickListener(this);
        mImgSearch = (ImageView) findViewById(R.id.img_search);

        mImgSearch.setOnClickListener(this);
        setSearchView();
    }
    ImageView imgClear=null;
    void  setClearIcon() {
        if (getPhrase() != null && getPhrase().length() > 0) {
            view.setVisibility(imgClear, true);
            imgClear.setImageResource(R.drawable.search_clear);
        }
        else {
            if (DataSource.haveHistory(this)) {
                view.setVisibility(imgClear, true);
                imgClear.setImageResource(R.drawable.search_clear_history);
            }else view.setVisibility(imgClear, false);
        }
    }
    MessageDialog mDialog=null;
    void clearHistory(){
        mDialog=new MessageDialog(this);

        mDialog.setOnDialogResultListener(new MessageDialog.OnDialogResultListener() {
            @Override
            public void OnDialogResult(boolean ok, String key) {
                if (!ok)return;
                DataSource.getDataSource().cleanTable("suggests",false);
                view.setVisibility(imgClear, false);
            }
        });
        mDialog.show(R.string.search_clear_history_title,R.string.search_clear_history_desc,"delete_search_history");
    }
    void setSearchView(){
//        if (mRichWebView == null) {
            mRichWebView = (RichWebView) findViewById(R.id.rich_web_view);
            mRichWebView.setOnUrlClickListener(new ViewHtml.OnUrlClickListener() {
                @Override
                public boolean onClick(WebView view, String url) {
                   return onSearchedItemClicked(url);

                }

                @Override
                public boolean onClicked(WebView view, String url) {
               return true;
                }
            });
//        }
        mNext=findViewById(R.id.btn_next);
        mPrev=findViewById(R.id.btn_prev);
        Text.setText(mNext, R.string.search_next, Font.TextPos.h1);
        Text.setText(mPrev, R.string.search_prev, Font.TextPos.h1);
        view.setVisibility(mNext, false);
        view.setVisibility(mPrev, false);
        mNext.setOnClickListener(this);
        mPrev.setOnClickListener(this);
//        findViewById(R._id.img_search).setOnClickListener(this);
        mEtxtSearch = (AutoCompleteTextView) findViewById(R.id.etxt_search);
        Text.setHint(mEtxtSearch, R.string.hint_search, Font.TextPos.h1);
        imgClear = (ImageView) findViewById(R.id.img_clear);
        setClearIcon();
//        view.setVisibility(imgClear,getPhrase()!=null&& getPhrase().length() > 0);
        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPhrase()!=null&& getPhrase().length() > 0)
                    mEtxtSearch.setText(null);

                else clearHistory();
            }
        });
        if(!Text.isNullOrEmpty(getPhrase()))
        {
            mEtxtSearch.setText(getPhrase());
            search(null);
        }
        mEtxtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEtxtSearch.isPopupShowing())  showSuggest();
            }
        });
        mEtxtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    getInputMethodManager().showSoftInput(mEtxtSearch, InputMethodManager.SHOW_IMPLICIT);

                }
                else getInputMethodManager().hideSoftInputFromWindow(mEtxtSearch.getWindowToken(), 0);
            }
        });
        mEtxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s0 = (s==null)?"":s.toString();
//                view.setVisibility(imgClear, s0.length() > 0);

                if (s0.equals(getPhrase())) return;
                setPhrase(s0);setClearIcon();
                showSuggest();


            }
        });
        mEtxtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search(null);
            }
        });
        mEtxtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    search();
                    search(null);
                    return true;
                }
                return false;
            }
        });

    }
   public void showSuggest(){
        if (mEtxtSearch.getAdapter() == null) {
            SuggestionAdapter mSuggestAdapter=new SuggestionAdapter(ActivitySearch.this );
            mEtxtSearch.setAdapter(mSuggestAdapter);
            mEtxtSearch.setThreshold(100);
            mEtxtSearch.setDropDownVerticalOffset(1);
            mEtxtSearch.setDropDownBackgroundResource(R.drawable.back_suggest_dropdown);
        }
        ((SuggestionAdapter)mEtxtSearch.getAdapter()).fill(
                Text.isNullOrEmpty(getPhrase()) ?
                        DataSource.getHistory(ActivitySearch.this,20):
                        getDataSource().getSuggest( getPhrase(),mSearchOptions.getInAllCategories() ? "": mCurrentCategory, 5, 0));
        mEtxtSearch.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEtxtSearch.showDropDown();
            }
        }, 200);
    }
    public View mNext,mPrev;
   public Map<String,String> mOptions = null;
    public void search( Boolean next) {
        if (Text.isNullOrEmpty(getPhrase()))return;
        if (mWaitingDialog==null||!mWaitingDialog.isShowing())mWaitingDialog=new WaitingDialog(ActivitySearch.this);
        mWaitingDialog.show();
        beNext=next;
        if (next == null) {
            mOffset = 0;
            view.setVisibility(mNext, false);
        } else if (!next) {
            if (mOffset > 0) mOffset -= mLimit;
        } else {
            mOffset += mLimit;
        }

        view.setVisibility(mPrev, mOffset > 0);
        search();
    }
public void search(){
    SearchAsync searchAsync=new SearchAsync();
    searchAsync.execute();
}
   public Map<String,String> mOptionsRegex = new HashMap<>();
   public WaitingDialog mWaitingDialog=null;
   public String getPhrase() {
        return getIntent().getStringExtra("phrase");
    }
   public Boolean beNext;
   public void setPhrase(String phrase) {
       phrase= phrase.replaceAll(" +$","");
        getIntent().putExtra("phrase",phrase);
        mOffset=0;


        if (mCategory == null) {
            if (Text.isEmpty(mCurrentCategory)) {
                mCategory = getDataSource().getCategoryByTag(mRootTag);
            } else
                mCategory = getDataSource().getCategory(mCurrentCategory);
        }
        if (mCategory == null) return;

        if (mCategory.hasSearchOption() )
            mOptions = mCategory.getSearchPairs(phrase.replace("'","''"), Text.isNullOrEmpty(mRootTag));
        else {
            Category root = getDataSource().getSearchOptionCategory(mCategory._id);
            mOptions = root.getSearchPairs(getPhrase().replace("'","''"), true);
        }
        mOptionsRegex=new HashMap<>();
       for (Map.Entry<String, String> stringStringEntry : mOptions.entrySet()) {
           mOptionsRegex.put(stringStringEntry.getKey(),fixRegex(stringStringEntry.getValue()));
       }
//        for (NameValuePair option : mOptions) {
//            mOptionsRegex.add(new NameValuePair(option.getName(), fixRegex((String) option.getValue())));
//        }
//        showNavigation(false);
    }

    public boolean onSearchedItemClicked(String url) {
        finish();
        return true;
    }

    String fixRegex(String input) {
        input = input.substring(1, input.length() - 1);
        return input.toLowerCase().replace(".", "\\.").replace("\"", "\\\"").replace("%", ".*").replace("_", ".");
    }
    public void onResult(List<?> items) {
        mItems =items;
        if (beNext==null&&items.size()>0) DataSource.registHistory(this,new Suggest(getPhrase()));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(mNext, mItems.size() == mLimit);

                if (!setItems((List<Content>) mItems,true)&& mItems.size() == mLimit)
                    search(beNext);
                mWaitingDialog.hide();}
        });
    }
     boolean setItems(List<Content> items,boolean noCase) {

        StringBuilder sb = new StringBuilder();
        for (Content content : items) {
            content.getSearchedHtml(sb,getDataSource(), mOptionsRegex,noCase);
//            if (!Text.isNullOrEmpty(string)) sb.append(string);
        }

//        String content = sb.toString();
        mRichWebView.setContent(sb.toString(), false, 5);
        return sb.length()>0;
    }

    public DataSource getDataSource() {
//        if (mDataSource == null) mDataSource = new DataSource(this);
        return DataSource.getDataSource();
    }

    InputMethodManager getInputMethodManager() {
        if (imm == null) imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm;
    }

    @Override
    public void onBackPressed() {
        if (mEtxtSearch.length()>0&& getInputMethodManager().isActive(mEtxtSearch) && getInputMethodManager().isAcceptingText()) {
            getInputMethodManager().hideSoftInputFromWindow(mEtxtSearch.getWindowToken(), 0);
            mRichWebView.requestFocus();

        } else {
            super.onBackPressed();
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_header_setting2 || v.getId() == R.id.img_header_settings)
            mDrawerLayout.openDrawer(Gravity.LEFT);
        else if (id == R.id.img_header_back) finish();
        else if (id == R.id.img_header_icon1) mEtxtSearch.setText(null);
        else if (id == R.id.img_search) search(null);
        else if (id == R.id.btn_next) search( true);
        else if (id == R.id.btn_prev) search(false);
    }

    class SearchAsync extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            onResult(getDataSource().search(mOptions, mSearchOptions.getInAllCategories() ? "" : mCurrentCategory, getPhrase(), mLimit, mOffset));
            return null;
        }
    }
}
