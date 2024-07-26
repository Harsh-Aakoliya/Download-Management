import java.util.ArrayList;
import java.util.List;

class Download implements Runnable {
    private static final int MAX_BUFFER_SIZE = 1024;

    public static final String STATUSES[] = { "Downloading", "Paused", "Complete", "Cancelled", "Error" };

    public String name;

    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;

    private float size; // size of download in bytes
    private float speed;
    private float downloaded; // number of bytes downloaded
    private int status; // current status of download

    private List<DownloadObserver> observers = new ArrayList<>();

    // Constructor for Download.
    public Download(String name, Float size, Float speed) {
        this.name = name;
        this.size = size;
        this.speed = speed;
        downloaded = 0;
        status = DOWNLOADING;

        // Begin the download.
        download();
    }

    // Get this download's URL.
    public String getName() {
        return name;
    }

    // Get this download's size.
    public float getSize() {
        return size;
    }

    // Get this download's progress.
    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }

    public int getStatus() {
        return status;
    }

    public void pause() {
        status = PAUSED;
        stateChanged();
    }

    public void resume() {
        status = DOWNLOADING;
        stateChanged();
        download();
    }

    public void cancel() {
        status = CANCELLED;
        stateChanged();
    }

    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    // Simulate download file.
    public void run() {
        while(downloaded <= size) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            if(status == DOWNLOADING)
            {
                downloaded += speed;
                stateChanged();
            }
        }

        if (status == DOWNLOADING) {
            status = COMPLETE;
            stateChanged();
        }
    }

    private void stateChanged() {
        notifyObservers();
    }

    public void addObserver(DownloadObserver observer) {
        observers.add(observer);
    }

    public void deleteObserver(DownloadObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (DownloadObserver observer : observers) {
            observer.update(this);
        }
    }
}

interface DownloadObserver {
    void update(Download download);
}