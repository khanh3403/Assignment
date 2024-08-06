package ASsigment.fptpoly.qqbook.Adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Spacing extends RecyclerView.ItemDecoration {
    private final int vericalSpacing;

    public Spacing(int vericalSpacing) {
        this.vericalSpacing = vericalSpacing;
    }
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state){
        outRect.bottom = vericalSpacing;
    }
}
