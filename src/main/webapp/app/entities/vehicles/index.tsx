import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Vehicles from './vehicles';
import VehiclesDetail from './vehicles-detail';
import VehiclesUpdate from './vehicles-update';
import VehiclesDeleteDialog from './vehicles-delete-dialog';

const VehiclesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Vehicles />} />
    <Route path="new" element={<VehiclesUpdate />} />
    <Route path=":id">
      <Route index element={<VehiclesDetail />} />
      <Route path="edit" element={<VehiclesUpdate />} />
      <Route path="delete" element={<VehiclesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehiclesRoutes;
