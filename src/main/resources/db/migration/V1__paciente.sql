create type sexo as enum('MASCULINO', 'FEMININO');

create type tipo_cancer as enum(
	'MAMA',
	'PROSTATA',
	'PULMAO',
	'COLON_RETO',
	'ESTOMAGO',
	'FIGADO',
	'COLO_UTERO',
	'ESOFAGO',
	'LEUCEMIA',
	'LINFOMA',
	'PELE',
	'BEXIGA',
	'RIM',
	'PANCREAS',
	'TIREOIDE',
	'OUTROS'
);

create type etapa_atual_cuidado as enum(
	'DIAGNOSTICO',
	'ESTADIAMENTO',
	'INICIO_TRATAMENTO',
	'EM_TRATAMENTO',
	'RECIDIVA'
);

create type condicao_socioeconomica as enum('VULNERAVEL', 'REGULAR');

create type escolaridade as enum(
	'FUNDAMENTAL_INCOMPLETO',
	'ENSINO_MEDIO',
	'ENSINO_SUPERIOR'
);

create type prioridade as enum('ALTA', 'MEDIA', 'BAIXA');

create table if not exists paciente (
	id uuid not null primary key,
	nome_completo text not null,
	dt_nascimento timestamp with time zone,
	sexo sexo,
	cartao_sus bigint,
	municipio_residencia text,
	distancia_centro_oncologico integer,
	tipo_cancer tipo_cancer,
	tnm integer check (
		tnm >= 0
		and tnm <= 4
	),
	ecog integer check (
		ecog >= 0
		and ecog <= 4
	),
	presenca_metastase boolean,
	comorbidades_relevantes boolean,
	dor_intensa boolean,
	sangramento_ativo boolean,
	perda_peso boolean,
	dt_diagnostico date,
	dt_encaminhamento date,
	exames_imagem boolean,
	biopsia boolean,
	imuno_histoquimica boolean,
	etapa_atual_cuidado etapa_atual_cuidado,
	condicao_socioeconomica condicao_socioeconomica,
	escolaridade escolaridade,
	cuidador_ausente boolean,
	prioridade prioridade,
	pontuacao_geografica integer,
	pontuacao_clinica integer,
	pontuacao_assistencial integer,
	pontuacao_sociodemografica integer,
	pontuacao_total integer
);