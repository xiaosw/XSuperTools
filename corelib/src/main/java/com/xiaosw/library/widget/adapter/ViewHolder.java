package com.xiaosw.library.widget.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * @ClassName  : {@link ViewHolder}
 * @Description: 适配器中的 ViewHolder 类
 * @link 参考：http://www.piwai.info/android-adapter-good-practices/#Update
 *
 * @date 2015-10-28下午4:20:02
 * @Author xiaoshiwang <xiaoshiwang@rytong.com>
 */
public class ViewHolder {

    @SuppressWarnings("unchecked")
    public static <T extends View> T getView(View convertView, int id) {

        SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
        if( holder == null ) {
            holder = new SparseArray<View>();
            convertView.setTag(holder);
        }

        View view = holder.get(id);
        if( view == null ) {
            view = convertView.findViewById(id);
            holder.put(id, view);
        }
        return (T)view;
    }
}