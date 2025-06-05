import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const errorRate = new Rate('errors');

export const options = {
  scenarios: {
    // Teste de smoke: verifica se a aplicação funciona com carga mínima
    smoke: {
      executor: 'constant-vus',
      vus: 1,
      duration: '30s',
      exec: 'smoke',
    },
    // Teste de carga: simula uso normal da aplicação
    load: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '30s', target: 50 },   // Sobe para 50 usuários em 30 segundos
        { duration: '30s', target: 50 },   // Mantém 50 usuários por 30 segundos
        { duration: '30s', target: 0 },    // Desce para 0 usuários em 30 segundos
      ],
      exec: 'load',
    },
    // Teste de stress: verifica os limites da aplicação
    stress: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '30s', target: 100 },  // Sobe para 100 usuários em 30 segundos
        { duration: '30s', target: 100 },  // Mantém 100 usuários por 30 segundos
        { duration: '30s', target: 0 },    // Desce para 0 usuários em 30 segundos
      ],
      exec: 'stress',
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% das requisições devem responder em menos de 500ms
    'http_req_duration{scenario:smoke}': ['p(95)<200'], // Mais restritivo para smoke test
    errors: ['rate<0.1'], // Taxa de erro deve ser menor que 10%
  },
};

// Use o nome do serviço do Docker Compose quando executado via container
const BASE_URL = __ENV.DOCKER_COMPOSE ? 'http://app:8080' : 'http://localhost:8080';

// Função auxiliar para gerar dados aleatórios de voto
function generateRandomVote() {
  const options = ['Java', 'Python', 'JavaScript', 'C#', 'Go'];
  return {
    language: options[Math.floor(Math.random() * options.length)],
  };
}

// Cenário de smoke test
export function smoke() {
  const payload = generateRandomVote();
  const res = http.post(`${BASE_URL}/votes`, JSON.stringify(payload), {
    headers: { 'Content-Type': 'application/json' },
  });
  
  check(res, {
    'status is 201': (r) => r.status === 201,
    'response time < 200ms': (r) => r.timings.duration < 200,
  }) || errorRate.add(1);

  // Consulta o ranking
  const rankingRes = http.get(`${BASE_URL}/ranking`);
  check(rankingRes, {
    'ranking status is 200': (r) => r.status === 200,
    'ranking response time < 200ms': (r) => r.timings.duration < 200,
  }) || errorRate.add(1);

  sleep(1);
}

// Cenário de teste de carga
export function load() {
  const payload = generateRandomVote();
  const res = http.post(`${BASE_URL}/votes`, JSON.stringify(payload), {
    headers: { 'Content-Type': 'application/json' },
  });
  
  check(res, {
    'status is 201': (r) => r.status === 201,
    'response time < 500ms': (r) => r.timings.duration < 500,
  }) || errorRate.add(1);

  if (Math.random() < 0.3) { // 30% das vezes consulta o ranking
    const rankingRes = http.get(`${BASE_URL}/ranking`);
    check(rankingRes, {
      'ranking status is 200': (r) => r.status === 200,
    }) || errorRate.add(1);
  }

  sleep(Math.random()); // Pausa aleatória entre 0 e 1 segundo
}

// Cenário de teste de stress
export function stress() {
  const payload = generateRandomVote();
  const res = http.post(`${BASE_URL}/votes`, JSON.stringify(payload), {
    headers: { 'Content-Type': 'application/json' },
  });
  
  check(res, {
    'status is 201': (r) => r.status === 201,
  }) || errorRate.add(1);

  if (Math.random() < 0.1) { // 10% das vezes consulta o ranking
    const rankingRes = http.get(`${BASE_URL}/ranking`);
    check(rankingRes, {
      'ranking status is 200': (r) => r.status === 200,
    }) || errorRate.add(1);
  }

  sleep(Math.random() * 0.5); // Pausa aleatória entre 0 e 0.5 segundos
} 