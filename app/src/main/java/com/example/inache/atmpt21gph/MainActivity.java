package com.example.inache.atmpt21gph;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



import cz.msebera.android.httpclient.Header;



public class MainActivity extends AppCompatActivity {

    private final static int DEFAULT_OFFSET = 0;
    private final static int MAX_GIFS = 10;


    private EditText qSearch;
    private ListView lvResults;
    private Adapter adapter;
    private ArrayList<Gif> gifs = new ArrayList<>();
    private Httpclient httpclient = new Httpclient();
    private ResponseParser parser = new ResponseParser();
    private boolean searchFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       initializeView();
       loadTrending(DEFAULT_OFFSET);
       lvResults.setOnScrollListener(new EndlessScrollListener() {
           @Override
           public void onLoadMore(int page) {
               if (searchFlag){
                   loadSearch(page);
               }else{
                   int newPage = page * MAX_GIFS;
                   loadTrending(newPage);
               }
           }
       });



    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            /**
             * It gets into the above IF-BLOCK if anywhere the screen is touched.
             */

            View v = getCurrentFocus();
            if ( v instanceof EditText) {


                /**
                 * Now, it gets into the above IF-BLOCK if an EditText is already in focus, and you tap somewhere else
                 * to take the focus away from that particular EditText. It could have 2 cases after tapping:
                 * 1. No EditText has focus
                 * 2. Focus is just shifted to the other EditText
                 */

                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }



    private void initializeView() {
        qSearch = findViewById(R.id.qSearch);
        lvResults = findViewById(R.id.lvRes);
        adapter = new Adapter(this,gifs);
        lvResults.setAdapter(adapter);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),Act2.class);
                Gif gif = gifs.get(position);
                intent.putExtra("url", gif.getUrl());
                startActivity(intent);
            }
        });

        final Handler handler = new Handler();
        final Runnable postToServerRunnable = new Runnable() {
            @Override
            public void run() {
                searchFlag = true;
                adapter.clear();
                loadSearch(DEFAULT_OFFSET);

            }
        };
// Live-search, idea, delay 500
        qSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // remove existing callback (timer reset)
                handler.removeCallbacks(postToServerRunnable);
                // 500 millisecond delay. Change to whatever delay you want.
                handler.postDelayed(postToServerRunnable, 500);
                //InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //in.hideSoftInputFromWindow(qSearch.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }





    public void onSearchClicked (View view){
        searchFlag = true;
        adapter.clear();
        loadSearch(DEFAULT_OFFSET);
        View currentView = this.getCurrentFocus();
        if (currentView != null){


            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(qSearch.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



    private void loadTrending(int offset) {
        httpclient.getTrendingGifs(offset, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                List<Gif> gifArraylist = parser.parseGifs(response);
                adapter.addAll(gifArraylist);
            }
        });
    }
    public void loadSearch(int offset){
        int newOffset = offset * MAX_GIFS;
        String searchQuery = qSearch.getText().toString();

        JsonHttpResponseHandler jshrh  = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                List<Gif> gifArraylist = parser.parseGifs(response);
                adapter.addAll(gifArraylist);
            }
        };
        if (qSearch.equals("")){
            httpclient.getTrendingGifs(offset,jshrh);
            Toast.makeText(MainActivity.this,"TYPE SOMETHING",Toast.LENGTH_LONG).show();
            return;
        }else {
            httpclient.getSearchGifs(newOffset,searchQuery,jshrh);
        }

    }
}





