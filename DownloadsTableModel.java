import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;


class DownloadsTableModel extends AbstractTableModel implements DownloadObserver {
    private static final String[] columnNames = { "File Name", "Size", "Progress", "Status" };
    private static final Class[] columnClasses = { String.class, String.class, JProgressBar.class, String.class };

    private ArrayList<Download> downloadList = new ArrayList<Download>();

    public void addDownload(Download download) {
        download.addObserver(this);
        downloadList.add(download);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public Download getDownload(int row) {
        return downloadList.get(row);
    }

    public void clearDownload(int row) {
        downloadList.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    public int getRowCount() {
        return downloadList.size();
    }

    public Object getValueAt(int row, int col) {
        Download download = downloadList.get(row);
        switch (col) {
            case 0: // Name
                return download.getName();
            case 1: // Size
                float size = download.getSize();
                return (size == -1) ? "" : Float.toString(size);
            case 2: // Progress
                return download.getProgress();
            case 3: // Status
                return Download.STATUSES[download.getStatus()];
        }
        return "";
    }

    @Override
    public void update(Download download) {
        int index = downloadList.indexOf(download);
        fireTableRowsUpdated(index, index);
    }
}