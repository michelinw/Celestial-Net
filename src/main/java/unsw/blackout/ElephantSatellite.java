package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;

import unsw.utils.Angle;

public class ElephantSatellite extends Satellite {
    private static final double SAT_LINEAR_VELOCITY = 2500;
    private static final String[] SUPPORT_DEVICE = {
            "Desktop", "Laptop"
    };
    private static final int MAX_FILES = Integer.MAX_VALUE;
    private static final int MAX_STORAGE = 90;
    private static final int MAX_RECV_BANDWIDTH = 20;
    private static final int MAX_SEND_BANDWIDTH = 20;
    private static final int MAX_RANGE = 400000;

    public ElephantSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
        this.setLinearVelocity(SAT_LINEAR_VELOCITY);
        this.setSupportedDevices(SUPPORT_DEVICE);
        this.setMaxFiles(MAX_FILES);
        this.setMaxRecvBandwidth(MAX_RECV_BANDWIDTH);
        this.setMaxSendBandwidth(MAX_SEND_BANDWIDTH);
        this.setMaxStorage(MAX_STORAGE);
        this.setMaxRange(MAX_RANGE);
    }

    @Override
    public void simulate() {
        this.standardSatMovement();
        this.normalTransferFile(this);
    }

    @Override
    public boolean hasStorage(int newFileSize) {
        FileManager fm = this.getFileManager();
        int totalFileCount = fm.getFiles().size();
        if (totalFileCount < this.getMaxFiles()) {
            if (fm.getTotalFileSize() + newFileSize < this.getMaxStorage()) {
                return true;
            }
        }

        ArrayList<File> transientFiles = fm.getInTransientFiles();

        int remainingSpace = this.getMaxStorage() - fm.getNonTransientFileSize() - newFileSize; // total limit of the
                                                                                                // bytes we need to keep

        if (remainingSpace < 0) {
            return false;
        } else if (remainingSpace == 0) {
            for (File file : transientFiles) {
                fm.removeFile(file.getFilename());
            }
            return true;
        }

        int size = transientFiles.size();
        HashMap<String, File> newFiles = knapSack(remainingSpace, size, transientFiles);
        boolean removed = false;
        for (File f : transientFiles) {
            if (!newFiles.containsKey(f.getFilename())) {
                fm.removeFile(f.getFilename());
                removed = true;
            }
        }
        return removed;
    }

    private HashMap<String, File> knapSack(int totalWeight, int n, ArrayList<File> oldFiles) {
        int i;
        int w;
        int[][] k = new int[n + 1][totalWeight + 1];

        for (i = 0; i <= n; i++) {
            for (w = 0; w <= totalWeight; w++) {
                if (i == 0 || w == 0) {
                    k[i][w] = 0;
                } else if (oldFiles.get(i - 1).getFileSize() <= w) {
                    k[i][w] = Math.max(
                            oldFiles.get(i - 1).getCompletedSize() + k[i - 1][w - oldFiles.get(i - 1).getFileSize()],
                            k[i - 1][w]);
                } else {
                    k[i][w] = k[i - 1][w];
                }
            }
        }

        // stores the result of Knapsack
        int res = k[n][totalWeight];
        HashMap<String, File> newTransFiles = new HashMap<String, File>();

        w = totalWeight;
        for (i = n; i > 0 && res > 0; i--) {

            // either the result comes from the top
            // (K[i-1][w]) or from (val[i-1] + K[i-1]
            // [w-wt[i-1]]) as in Knapsack table. If
            // it comes from the latter one, then means
            // the item is included.
            if (res == k[i - 1][w]) {
                continue;
            } else {
                // This item is included.
                newTransFiles.put(oldFiles.get(i - 1).getFilename(), oldFiles.get(i - 1));

                // Since this weight is included its
                // value is deducted
                res = res - oldFiles.get(i - 1).getCompletedSize();
                w = w - oldFiles.get(i - 1).getFileSize();
            }
        }
        return newTransFiles;
    }

}
