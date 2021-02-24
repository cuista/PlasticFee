insert into pfe_user(id, password, reputation, username) values (1,'password1',0,'utente1');
insert into pfe_user(id, password, reputation, username) values (2,'tuborg',0,'greta');

INSERT INTO pfe_proposal(id, is_active, creation_datetime, description, expiration_date, reputation_reward, title, creator_id) values (11,true,CURRENT_TIMESTAMP(6),'descr1','2021-02-25',12,'title1',2);
INSERT INTO pfe_proposal(id, is_active, creation_datetime, description, expiration_date, reputation_reward, title, creator_id) values (12,true,CURRENT_TIMESTAMP(6),'descr2','2021-06-25',43,'title2',1);

INSERT INTO pfe_vote(id, is_in_favor, proposal, user) values (21,false,11,2);
INSERT INTO pfe_vote(id, is_in_favor, proposal, user) values (22,true,12,1);