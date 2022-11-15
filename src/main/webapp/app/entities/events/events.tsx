import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEvents } from 'app/shared/model/events.model';
import { searchEntities, getEntities } from './events.reducer';

export const Events = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const eventsList = useAppSelector(state => state.events.entities);
  const loading = useAppSelector(state => state.events.loading);
  const totalItems = useAppSelector(state => state.events.totalItems);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    } else {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
  };

  const startSearching = e => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="events-heading" data-cy="EventsHeading">
        <Translate contentKey="rfidh2ElkApp.events.home.title">Events</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rfidh2ElkApp.events.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/events/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rfidh2ElkApp.events.home.createLabel">Create new Events</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('rfidh2ElkApp.events.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {eventsList && eventsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="rfidh2ElkApp.events.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('guid')}>
                  <Translate contentKey="rfidh2ElkApp.events.guid">Guid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('plate')}>
                  <Translate contentKey="rfidh2ElkApp.events.plate">Plate</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('anpr')}>
                  <Translate contentKey="rfidh2ElkApp.events.anpr">Anpr</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('rfid')}>
                  <Translate contentKey="rfidh2ElkApp.events.rfid">Rfid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dataStatus')}>
                  <Translate contentKey="rfidh2ElkApp.events.dataStatus">Data Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gantry')}>
                  <Translate contentKey="rfidh2ElkApp.events.gantry">Gantry</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('kph')}>
                  <Translate contentKey="rfidh2ElkApp.events.kph">Kph</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ambush')}>
                  <Translate contentKey="rfidh2ElkApp.events.ambush">Ambush</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('direction')}>
                  <Translate contentKey="rfidh2ElkApp.events.direction">Direction</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('vehicle')}>
                  <Translate contentKey="rfidh2ElkApp.events.vehicle">Vehicle</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('issue')}>
                  <Translate contentKey="rfidh2ElkApp.events.issue">Issue</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="rfidh2ElkApp.events.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('handledBy')}>
                  <Translate contentKey="rfidh2ElkApp.events.handledBy">Handled By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gantryProcessed')}>
                  <Translate contentKey="rfidh2ElkApp.events.gantryProcessed">Gantry Processed</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gantrySent')}>
                  <Translate contentKey="rfidh2ElkApp.events.gantrySent">Gantry Sent</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('when')}>
                  <Translate contentKey="rfidh2ElkApp.events.when">When</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('toll')}>
                  <Translate contentKey="rfidh2ElkApp.events.toll">Toll</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ruleRcvd')}>
                  <Translate contentKey="rfidh2ElkApp.events.ruleRcvd">Rule Rcvd</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('wantedFor')}>
                  <Translate contentKey="rfidh2ElkApp.events.wantedFor">Wanted For</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('fine')}>
                  <Translate contentKey="rfidh2ElkApp.events.fine">Fine</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('licenseIssue')}>
                  <Translate contentKey="rfidh2ElkApp.events.licenseIssue">License Issue</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('wantedBy')}>
                  <Translate contentKey="rfidh2ElkApp.events.wantedBy">Wanted By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ruleProcessed')}>
                  <Translate contentKey="rfidh2ElkApp.events.ruleProcessed">Rule Processed</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('speedFine')}>
                  <Translate contentKey="rfidh2ElkApp.events.speedFine">Speed Fine</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lane')}>
                  <Translate contentKey="rfidh2ElkApp.events.lane">Lane</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tagIssue')}>
                  <Translate contentKey="rfidh2ElkApp.events.tagIssue">Tag Issue</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('statusName')}>
                  <Translate contentKey="rfidh2ElkApp.events.statusName">Status Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('licenseFine')}>
                  <Translate contentKey="rfidh2ElkApp.events.licenseFine">License Fine</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('stolen')}>
                  <Translate contentKey="rfidh2ElkApp.events.stolen">Stolen</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('wanted')}>
                  <Translate contentKey="rfidh2ElkApp.events.wanted">Wanted</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ruleSent')}>
                  <Translate contentKey="rfidh2ElkApp.events.ruleSent">Rule Sent</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('handled')}>
                  <Translate contentKey="rfidh2ElkApp.events.handled">Handled</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ruleIssue')}>
                  <Translate contentKey="rfidh2ElkApp.events.ruleIssue">Rule Issue</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {eventsList.map((events, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/events/${events.id}`} color="link" size="sm">
                      {events.id}
                    </Button>
                  </td>
                  <td>{events.guid}</td>
                  <td>{events.plate}</td>
                  <td>{events.anpr}</td>
                  <td>{events.rfid}</td>
                  <td>{events.dataStatus}</td>
                  <td>{events.gantry}</td>
                  <td>{events.kph}</td>
                  <td>{events.ambush}</td>
                  <td>{events.direction}</td>
                  <td>{events.vehicle}</td>
                  <td>{events.issue}</td>
                  <td>{events.status}</td>
                  <td>{events.handledBy}</td>
                  <td>{events.gantryProcessed}</td>
                  <td>{events.gantrySent}</td>
                  <td>{events.when}</td>
                  <td>{events.toll}</td>
                  <td>{events.ruleRcvd}</td>
                  <td>{events.wantedFor}</td>
                  <td>{events.fine}</td>
                  <td>{events.licenseIssue}</td>
                  <td>{events.wantedBy}</td>
                  <td>{events.ruleProcessed}</td>
                  <td>{events.speedFine}</td>
                  <td>{events.lane}</td>
                  <td>{events.tagIssue}</td>
                  <td>{events.statusName}</td>
                  <td>{events.licenseFine}</td>
                  <td>{events.stolen}</td>
                  <td>{events.wanted ? 'true' : 'false'}</td>
                  <td>{events.ruleSent}</td>
                  <td>{events.handled}</td>
                  <td>{events.ruleIssue}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/events/${events.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/events/${events.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/events/${events.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="rfidh2ElkApp.events.home.notFound">No Events found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={eventsList && eventsList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Events;
