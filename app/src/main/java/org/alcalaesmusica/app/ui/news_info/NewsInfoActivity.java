package org.alcalaesmusica.app.ui.news_info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import org.alcalaesmusica.app.R;
import org.alcalaesmusica.app.base.BaseActivity;
import org.alcalaesmusica.app.base.BasePresenter;
import org.alcalaesmusica.app.model.News;
import org.alcalaesmusica.app.ui.image_full.ImageFullActivity;

public class NewsInfoActivity extends BaseActivity implements NewsInfoView {

    private NewsInfoPresenter presenter;
    private TextView tvNewsTitle;
    private ImageView imgNews;
    private TextView tvNewsText;
    private Button btnNewsLink;

    private void findViews() {
        tvNewsTitle = (TextView)findViewById( R.id.tv_news_title );
        imgNews = (ImageView)findViewById( R.id.img_news );
        tvNewsText = (TextView)findViewById( R.id.tv_news_text );
        btnNewsLink = (Button)findViewById( R.id.btn_news_link );

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = NewsInfoPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);

        findViews();
        configureSecondLevelActivity();

        presenter.onCreate(getIntent());
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    @Override
    public void showNews(final News news) {
        tvNewsTitle.setText(news.getTitle());
        tvNewsText.setText(news.getText());

        if (news.getImage() != null) {

            Picasso.with(this)
                    .load(news.getImageUrlFull())
                    .into(imgNews);

            imgNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lauchImageZoom(news.getImageUrlFull());
                }
            });
        }

        if (news.hasValidLinkButton()) {
            btnNewsLink.setText(news.getBtn_text());
            btnNewsLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(news.getBtn_link())));
                }
            });
        } else {
            btnNewsLink.setVisibility(View.GONE);
        }
    }

    private void lauchImageZoom(Uri imageUrlFull) {

        startActivity(ImageFullActivity.newImageFullActivity(this, imageUrlFull.toString()));
    }
}
