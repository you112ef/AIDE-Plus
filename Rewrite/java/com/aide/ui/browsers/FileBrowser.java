package com.aide.ui.browsers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.aide.common.AppLog;
import com.aide.common.KeyStrokeDetector;
import com.aide.common.ListAdapterBase;
import com.aide.ui.AppCommands;
import com.aide.ui.AppFileIcons;
import com.aide.ui.GlobalKeyCommand;
import com.aide.ui.MainActivity;
import com.aide.ui.QuickActionMenu;
import com.aide.ui.ServiceContainer;
import com.aide.ui.command.FileBrowserCommand;
import com.aide.ui.firebase.FireBaseLogEvent;
import com.aide.ui.rewrite.R;
import com.aide.ui.services.FileBrowserService;
import com.aide.ui.util.FileSystem;
import com.aide.ui.views.CustomKeysListView;

import java.util.ArrayList;
import java.util.List;

import io.github.zeroaicy.aide.ui.view.BreadcrumbItem;
import io.github.zeroaicy.aide.ui.view.BreadcrumbView;
import jaxp.sun.org.apache.xalan.internal.templates.Constants;

public class FileBrowser extends LinearLayoutCompat implements
        FileBrowserService.a,
        com.aide.ui.browsers.a {


    protected View fY;
    private final QuickActionMenu WB;
    private final KeyStrokeDetector.KeyStrokeHandler jw;

    private final QuickActionMenu mb;

    private Adapter qp;


    public FileBrowser(Context context) {
        super(context);
        this.WB = new QuickActionMenu(ServiceContainer.getMainActivity(), R.menu.filebrowser_context_menu);
        this.mb = new QuickActionMenu(ServiceContainer.getMainActivity(), R.menu.git_context_menu);
        this.jw = (KeyStrokeDetector.KeyStrokeHandler) new GlobalKeyCommand(AppCommands.VH());
        EQ();
    }

    public FileBrowser(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.WB = new QuickActionMenu(ServiceContainer.getMainActivity(), R.menu.filebrowser_context_menu);
        this.mb = new QuickActionMenu(ServiceContainer.getMainActivity(), R.menu.git_context_menu);
        this.jw = (KeyStrokeDetector.KeyStrokeHandler) new GlobalKeyCommand(AppCommands.VH());
        EQ();
    }


    public static boolean we(String str) {
        if (!str.startsWith(".") && !"bin".equals(str) && !"obj".equals(str) && !"build".equals(str)) {
            if (!"gradle".equals(str)) {
                return false;
            }
        }
        return true;
    }

    @SuppressLint("InflateParams")
    private void EQ() {
        setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        LayoutInflater from = LayoutInflater.from(getContext());
        View inflate = from.inflate(R.layout.filebrowser, null);
        removeAllViews();
        addView(inflate);
        getListView().addHeaderView(from.inflate(R.layout.browser_header_new, null), null, false);
        ServiceContainer.getFileBrowserService().Zo(this);
        CustomKeysListView listView = getListView();
        Adapter gVar = new Adapter(this, null);
        this.qp = gVar;
        listView.setAdapter(gVar);
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FileBrowser.this.Mr((Data) listView.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                FileBrowser.this.Mr(null);
            }
        });
        listView.setOnKeyEventListener(new CustomKeysListView.OnKeyEventListener() {
            @Override
            public boolean onKeyDown(int i, KeyEvent keyEvent) {
                return ServiceContainer.getMainActivity().getKeyStrokeDetector().onKeyDown(i, keyEvent, FileBrowser.this.jw);

            }

            @Override
            public boolean onKeyUp(int i, KeyEvent keyEvent) {
                return ServiceContainer.getMainActivity().getKeyStrokeDetector().onKeyUp(i, keyEvent, FileBrowser.this.jw);
            }
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Data hVar = (Data) listView.getItemAtPosition(position);
            if (hVar == null) {
                return true;
            }
            FileBrowser.this.Mr(hVar);
            FileBrowser.this.XL(view, true);
            return true;
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Data hVar = (Data) listView.getItemAtPosition(position);
            if (hVar == null) {
                return;
            }
            if (hVar.Hw != null) {
                FileBrowser.this.fY = view;
                FireBaseLogEvent.tp("Browser command: " + hVar.Hw.getNameId());
                if (hVar.Hw.isVisible(false)) {
                    hVar.Hw.run();
                    return;
                }
                return;
            }
            if (hVar.DW) {
                FileBrowser.this.getActivity().openFile(hVar.FH);
                return;
            }
            if (hVar.DW()) {
                String str = hVar.FH;
                if (str != null) {
                    ServiceContainer.getFileBrowserService().Hw(str);
                }
                FileBrowser.this.getListView().setSelection(0);
                return;
            }
            String str2 = hVar.FH;
            if (str2 != null) {
                FileBrowser.this.J8(str2);
            }
            FileBrowser.this.getListView().setSelection(0);

        });
        QX();
    }

    public CustomKeysListView getListView() {
        return findViewById(R.id.filebrowserFileList);
    }

    public MainActivity getActivity() {
        return (MainActivity) getContext();
    }


    public void J8(String str) {

        if (ServiceContainer.getProjectService().isInCurrentProjectDirectory(str)) {
            while (true) {
                List<String> listFiles = FileSystem.listFiles(str);
                if (listFiles.size() != 1 || !FileSystem.notPreProcessorFile(listFiles.get(0))) {
                    break;
                } else {
                    str = listFiles.get(0);
                }
            }
        }
        ServiceContainer.getFileBrowserService().Hw(str);
    }

    public void Mr(Data data) {

        String str = null;
        if (data != null && (data.DW || data.j6())) {
            str = data.FH;
        }
        ServiceContainer.getFileBrowserService().VH(str);
    }

    public void XL(View view, boolean z) {
        this.WB.QX(view, z);
    }

    private void aM(View view) {
        this.mb.QX(view, true);
    }

    @Keep /* 用于给菜单上的一个按钮使用的 */
    public void J0() {
        Object selectedItem = getListView().getSelectedItem();
        if (selectedItem instanceof Data) {
            Mr((Data) selectedItem);
            XL(getListView().getSelectedView(), false);
        }
    }

    @Keep /* 外部请求使用了该api */
    public void QX() {
        String j6 = ServiceContainer.getFileBrowserService().j6();
        ServiceContainer.getFileBrowserService().VH(null);
        CustomKeysListView listView = getListView();
        /*((TextView) listView.findViewById(R.id.browserHeaderLabel)).setText(j6);*/

        // 新增面包屑列表
        BreadcrumbView breadcrumbView = listView.findViewById(R.id.browserHeaderBreadcrumb);

        breadcrumbView.setPath(createList(j6), (item,  index)->{

            J8(item.getFullPath());
            //ToastUtils.showLong("点击了："+item.getName()+"路径"+item.getFullPath());

            return null;
        });

        ((ImageView) listView.findViewById(R.id.browserHeaderIcon)).setImageResource(R.drawable.folder_open);
        ImageView imageView = listView.findViewById(R.id.browserHeaderMenuButton);
        imageView.setOnClickListener(v -> {
            ServiceContainer.getFileBrowserService().VH(null);
            FileBrowser.this.XL(imageView, true);
        });
        List<Data> arrayList = new ArrayList<>();
        String parent = FileSystem.getParent(j6);
        if (parent != null) {
            arrayList.add(new Data(this, parent, Constants.ATTRVAL_PARENT, false));
        }
        for (FileBrowserCommand fileBrowserCommand : AppCommands.getFileBrowserCommands()) {
            if (fileBrowserCommand.isVisible(false)) {
                arrayList.add(new Data(this, fileBrowserCommand));
            }
        }
        try {

            boolean isRoot = false;
            boolean isShizuku = false;
//            boolean isDefault = true; // 上面两个用不了就默认这个了
//            boolean isSAF = true; // 太垃圾了，不想用



            if (isRoot){



            } else if (isShizuku) {



            } else {

            }


            List<String> listFiles = FileSystem.listFiles(j6);
            sortList(listFiles);
            for (String str : listFiles) {
                arrayList.add(new Data(this, str, FileSystem.getName(str), FileSystem.KD(str)));
            }
        } catch (Exception e2) {
            AppLog.e(e2);
        }
        this.qp.DW(arrayList);
    }


    public void sortList(List<String> listFiles) {

    }

    public List<BreadcrumbItem> createList(String fullPath){
        List<BreadcrumbItem> items = new ArrayList<>();
        if (fullPath == null || fullPath.isEmpty()) return items;

        String[] segments = fullPath.replaceAll("^/+", "").split("/");
        StringBuilder currentPath = new StringBuilder();

        for (String segment : segments) {
            currentPath.append("/").append(segment);
            items.add(new BreadcrumbItem(segment, currentPath.toString()));
        }
        return items;
    }

    @Keep /* 外部请求使用了该api */
    public void Ws() {
        aM(this.fY);
    }

    @Keep
    @Override
    public void DW() {
        getListView().requestFocus();
    }

    @Keep
    @Override
    public void FH() {

    }

    @Override
    public void j6() {
        QX();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {

        if (ServiceContainer.getMainActivity().getKeyStrokeDetector().onKeyDown(i, keyEvent, this.jw)) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (ServiceContainer.getMainActivity().getKeyStrokeDetector().onKeyUp(i, keyEvent, this.jw)) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }


    static class Adapter extends ListAdapterBase<Data> {

        FileBrowser fileBrowser;

        public Adapter(FileBrowser fileBrowser) {
            this.fileBrowser = fileBrowser;
        }

        public Adapter(FileBrowser fileBrowser, AdapterView.OnItemSelectedListener listener) {
            this(fileBrowser);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = convertView == null ? LayoutInflater.from(fileBrowser.getContext()).inflate(R.layout.filebrowser_entry, parent, false) : convertView;
            Data j6 = j6(position);
            ((TextView) inflate.findViewById(R.id.filebrowserEntryName)).setText(j6.j6);
            ((ImageView) inflate.findViewById(R.id.filebrowserEntryFileImage)).setImageResource(j6.v5);
            return inflate;
        }
    }


    static class Data {
        final FileBrowser Zo;
        public boolean DW;
        public String FH;
        public FileBrowserCommand Hw;
        public String j6;

        public int v5;

        public Data(FileBrowser fileBrowser, String str, String str2, boolean z) {
            this.Zo = fileBrowser;
            this.FH = str;
            this.j6 = str2;
            this.DW = z;
            if (z) {
                this.v5 = AppFileIcons.j6(str);
                return;
            }
            if (DW()) {
                this.v5 = R.drawable.folder_open;
            } else if (FileBrowser.we(str2)) {
                this.v5 = R.drawable.folder_hidden;
            } else {
                this.v5 = R.drawable.folder;
            }

        }

        public Data(FileBrowser fileBrowser, FileBrowserCommand fileBrowserCommand) {
            this.Zo = fileBrowser;
            this.Hw = fileBrowserCommand;
            this.v5 = fileBrowserCommand.getIconId();
            int nameId = fileBrowserCommand.getNameId();
            if (nameId != 0) {
                this.j6 = fileBrowser.getContext().getResources().getString(nameId);
            }
        }


        public boolean DW() {
            if (!this.DW) {
                if (this.j6.equals(Constants.ATTRVAL_PARENT)) {
                    return true;
                }
            }
            return false;
        }

        public boolean j6() {
            if (!this.DW) {
                if (!DW()) {
                    return true;
                }
            }
            return false;
        }


    }


    static class ShellFile{

        Model model;

        public ShellFile(Model model){
            this.model = model;

        }



        enum Model{
            ROOT,
            SHIZUKU,
            DEFAULT
        }

    }
}
