import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Gantry from './gantry';
import GantryDetail from './gantry-detail';
import GantryUpdate from './gantry-update';
import GantryDeleteDialog from './gantry-delete-dialog';

const GantryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Gantry />} />
    <Route path="new" element={<GantryUpdate />} />
    <Route path=":id">
      <Route index element={<GantryDetail />} />
      <Route path="edit" element={<GantryUpdate />} />
      <Route path="delete" element={<GantryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GantryRoutes;
