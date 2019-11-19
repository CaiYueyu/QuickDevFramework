package com.linxiao.quickdevframework.sample.frameworkapi;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linxiao.framework.list.SingleItemAdapter;
import com.linxiao.framework.dialog.AlertDialogManager;
import com.linxiao.framework.architecture.BaseFragment;
import com.linxiao.framework.file.FileManager;
import com.linxiao.framework.permission.PermissionManager;
import com.linxiao.framework.permission.RequestPermissionCallback;
import com.linxiao.quickdevframework.R;
import com.linxiao.quickdevframework.sample.adapter.FileListAdapter;
import com.linxiao.quickdevframework.sample.divider.HorizontalDecoration;

import java.io.File;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件浏览Fragment
 * Created by linxiao on 2017/4/15.
 */
public class FileBrowserFragment extends BaseFragment {

    @BindView(R.id.tvPath)
    TextView tvPath;
    @BindView(R.id.rcvFileList)
    RecyclerView rcvFileList;

    private FileListAdapter fileListAdapter;

    private File currentPath;

    @Override
    protected void onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.fragment_file_browser, container);
        ButterKnife.bind(this, getContentView());
        initView();

        currentPath = FileManager.getExternalStorageRoot();
        loadPath(currentPath);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView() {
        rcvFileList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvFileList.setItemAnimator(new DefaultItemAnimator());
        rcvFileList.addItemDecoration(new HorizontalDecoration(1, new ColorDrawable(Color.BLACK)));

        fileListAdapter = new FileListAdapter(getContext());
        rcvFileList.setAdapter(fileListAdapter);
        fileListAdapter.setOnItemClickListener(new SingleItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SingleItemAdapter adapter, View itemView, int position) {

            }
        });

    }

    /**
     * 加载文件路径，将文件夹下的列表
     * */
    private void loadPath(final File path) {
        PermissionManager.createPermissionOperator()
        .requestSDCard()
        .doOnProhibited(permission -> AlertDialogManager.showAlertDialog("请授予文件管理权限以查看演示效果"))
        .perform(getActivity(), new RequestPermissionCallback() {
            @Override
            public void onGranted() {

                fileListAdapter.setDataSource(Arrays.asList(path.listFiles()));
                tvPath.setText(path.getName());
                currentPath = path;
            }

            @Override
            public void onDenied() {
                AlertDialogManager.showAlertDialog("未授予权限");
            }
        });
    }
}
