package com.serotonin.modbus4j;

import com.serotonin.util.ProgressiveTaskListener;

/**
 * @author Matthew Lohbihler
 */
public interface NodeScanListener extends ProgressiveTaskListener {
    void nodeFound(int nodeNumber);
}
