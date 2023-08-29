\c mydatabase;

CREATE TABLE pessoaentity (
    id uuid NOT NULL,
    apelido character varying(255),
    nascimento character varying(255),
    nome character varying(255),
    stack character varying(255)
);


ALTER TABLE pessoaentity OWNER TO sarah;

ALTER TABLE ONLY pessoaentity
    ADD CONSTRAINT pessoaentity_pkey PRIMARY KEY (id);


ALTER TABLE ONLY pessoaentity
    ADD CONSTRAINT uk1upmjfam63kbek6e9kui0wil3 UNIQUE (apelido);
