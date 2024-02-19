package io.devpl.codegen.samples.ui;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 剪贴板功能
 */
public class ClipboardUtils {
    /**
     * 把文件放到剪贴板，然后就可按ctrl+v粘贴了
     */
    public static void setFile(String file) {
        FileTransferable f = new FileTransferable();
        f.addFile(file);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(f, null);
    }

    static class FileTransferable implements Transferable {
        private DataFlavor[] dataFlavors = new DataFlavor[]{DataFlavor.javaFileListFlavor};

        private List<File> _files = new ArrayList<>();

        public void addFile(String file) {
            _files.add(new File(file));
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return dataFlavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            for (DataFlavor dataFlavor : dataFlavors) {
                if (dataFlavor.equals(flavor)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            return _files;
        }
    }
}

