import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EventsImages from './events-images';
import EventsImagesDetail from './events-images-detail';
import EventsImagesUpdate from './events-images-update';
import EventsImagesDeleteDialog from './events-images-delete-dialog';

const EventsImagesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EventsImages />} />
    <Route path="new" element={<EventsImagesUpdate />} />
    <Route path=":id">
      <Route index element={<EventsImagesDetail />} />
      <Route path="edit" element={<EventsImagesUpdate />} />
      <Route path="delete" element={<EventsImagesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EventsImagesRoutes;
