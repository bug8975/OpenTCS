/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.kernel.controlcenter.vehicles;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import static java.util.Objects.requireNonNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.opentcs.data.model.Vehicle;
import org.opentcs.drivers.vehicle.VehicleCommAdapter;
import org.opentcs.drivers.vehicle.VehicleCommAdapterFactory;
import org.opentcs.drivers.vehicle.VehicleProcessModel;

/**
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class VehicleEntry
    implements PropertyChangeListener {

  private final Vehicle vehicle;
  /**
   * Used for implementing property change events.
   */
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  private VehicleProcessModel processModel;

  private VehicleCommAdapterFactory commAdapterFactory = new NullVehicleCommAdapterFactory();

  private VehicleCommAdapter commAdapter;

  private int selectedTabIndex;

  public VehicleEntry(Vehicle vehicle) {
    this.vehicle = requireNonNull(vehicle, "vehicle");
    this.processModel = new VehicleProcessModel(vehicle);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (!(evt.getSource() instanceof VehicleProcessModel)) {
      return;
    }
    
    pcs.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }

  @Nonnull
  public Vehicle getVehicle() {
    return vehicle;
  }

  @Nonnull
  public VehicleProcessModel getProcessModel() {
    return processModel;
  }

  public void setProcessModel(@Nonnull VehicleProcessModel processModel) {
    VehicleProcessModel oldProcessModel = this.processModel;
    this.processModel = requireNonNull(processModel, "processModel");

    oldProcessModel.removePropertyChangeListener(this);
    processModel.addPropertyChangeListener(this);

    pcs.firePropertyChange(Attribute.PROCESS_MODEL.name(), oldProcessModel, processModel);
  }

  @Nullable
  public VehicleCommAdapterFactory getCommAdapterFactory() {
    return commAdapterFactory;
  }

  public void setCommAdapterFactory(@Nullable VehicleCommAdapterFactory commAdapterFactory) {
    VehicleCommAdapterFactory oldValue = this.commAdapterFactory;
    this.commAdapterFactory = commAdapterFactory;
    
    pcs.firePropertyChange(Attribute.COMM_ADAPTER_FACTORY.name(), oldValue, commAdapterFactory);
  }

  @Nullable
  public VehicleCommAdapter getCommAdapter() {
    return commAdapter;
  }

  public void setCommAdapter(@Nullable VehicleCommAdapter commAdapter) {
    VehicleCommAdapter oldValue = this.commAdapter;
    this.commAdapter = commAdapter;
    
    pcs.firePropertyChange(Attribute.COMM_ADAPTER.name(), oldValue, commAdapter);
  }

  public int getSelectedTabIndex() {
    return selectedTabIndex;
  }

  public void setSelectedTabIndex(int selectedTabIndex) {
    int oldValue = this.selectedTabIndex;
    this.selectedTabIndex = selectedTabIndex;
    
    pcs.firePropertyChange(Attribute.SELECTED_TAB_INDEX.name(), oldValue, selectedTabIndex);
  }

  /**
   * Enum elements used as notification arguments to specify which argument changed.
   */
  public static enum Attribute {
    /**
     * Indicates a change of the process model reference.
     */
    PROCESS_MODEL,
    /**
     * Indicates a change of the comm adapter factory reference.
     */
    COMM_ADAPTER_FACTORY,
    /**
     * Indicates a change of the comm adapter reference.
     */
    COMM_ADAPTER,
    /**
     * Indicates a change of the selected tab index.
     */
    SELECTED_TAB_INDEX
  }
}
