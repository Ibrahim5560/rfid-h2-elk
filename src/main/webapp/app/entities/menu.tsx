import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/images">
        <Translate contentKey="global.menu.entities.images" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/task">
        <Translate contentKey="global.menu.entities.task" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/events">
        <Translate contentKey="global.menu.entities.events" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
