import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Events from './events';
import EventsDetail from './events-detail';
import EventsUpdate from './events-update';
import EventsDeleteDialog from './events-delete-dialog';

const EventsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Events />} />
    <Route path="new" element={<EventsUpdate />} />
    <Route path=":id">
      <Route index element={<EventsDetail />} />
      <Route path="edit" element={<EventsUpdate />} />
      <Route path="delete" element={<EventsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EventsRoutes;
