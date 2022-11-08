import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/events">
        <Translate contentKey="global.menu.entities.events" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tasks">
        <Translate contentKey="global.menu.entities.tasks" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/gantry">
        <Translate contentKey="global.menu.entities.gantry" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicles">
        <Translate contentKey="global.menu.entities.vehicles" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
