create extension pg_trgm; -- Busca por similaridade
create extension btree_gin; -- PostgreSQL GIN index
alter database dev set pg_trgm.word_similarity_threshold = 0.4; -- Todas as palavras com word_similarity (<%) maior que 0.4 serão retornadas na consulta

drop index idx_paciente_nome_completo;
create index idx_paciente_nome_completo on paciente using gin(nome_completo);