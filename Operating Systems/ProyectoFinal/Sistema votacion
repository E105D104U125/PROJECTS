// Ganador de la ronda
        if (solucion != -1) {
            // Espera a los hilos
            for (i = 0; i < num_hilos; i++){
                pthread_join(h[i], NULL);
                fprintf(stderr, "Hilo %d finaliza\n", i);
            }
            //Pone en memoria compartida su supuesta solucion
            sem_wait(sem_mutex1);
            mem_block->solution = solucion;
            sem_post(sem_mutex1);

            // Quorum
            sem_wait(sem_mutex2);
            net_data->total_miners = 0;
            net_data->last_winner = getpid(); 
            for (i = 0; i < MAX_MINERS; i++){
                if (net_data->miners_pid[i] != -1 && net_data->miners_pid[i] != getpid()){
                    if (kill(net_data->miners_pid[i], SIGUSR1) == 0){
                        net_data->total_miners ++;
                    }
                    else{
                        net_data->miners_pid[i] = -1;
                    }
                }
                net_data->voting_pool[i] = -1;
            }
            sem_post(sem_mutex2);
            
            //Espera que acabe la votacion
            sigfillset(&set);
            sigdelset(&set, SIGUSR2);
            sigdelset(&set, SIGALRM);
            sigsuspend(&set);

            // Se ha corrompido la red, porque no hay votantes
            if (got_sigarlm){
                break;
            }

            // Se ha recibido SIGUSR2
            got_sigusr2 = 0;

            // Conteo de votos
            votosFavor = 0;
            votos = 0;
            sem_wait(sem_mutex2);
            for (i = 0; i < MAX_MINERS; i++){
                if(net_data->voting_pool[i] == 1){
                    votos ++;
                    votosFavor++;
                }
                else if(net_data->voting_pool[i] == 0){
                    votos ++;
                }
            }
            sem_post(sem_mutex2);

            flag_valid = 0;
            // Valida la votacion
            sem_wait(sem_mutex1);
            if(votosFavor >= votos - votosFavor){
                mem_block->is_valid = 1;
                flag_valid = 1;
            }
            else{
                mem_block->is_valid = 0;
            }
            sem_post(sem_mutex1);
            
            sem_wait(sem_mutex2);

            // Copia el bloque
            /*if (flag_valid == 1){
                // Suma 1 a su wallet
                for (i = 0; i < MAX_MINERS, net_data->miners_pid[i] != getpid(); i++);

                mem_block->wallets[i] ++;
            }*/

            // Despierta a los perdedores
            for (i = 0, net_data->total_miners = 0; i < MAX_MINERS; i++){
                if (net_data->miners_pid[i] != -1){
                    kill(net_data->miners_pid[i], SIGUSR2);
                }
                net_data->total_miners ++;
            }
            sem_post(sem_mutex2);
            
            // Espera a que copien los perdedores
            sigfillset(&set);
            sigdelset(&set, SIGUSR2);
            sigdelset(&set, SIGALRM);
            sigsuspend(&set);
            
            // Se ha corrompido la red, porque no hay votantes
            if (got_sigarlm){
                break;
            }

            // Creacion del nuevo bloque
            sem_wait(sem_mutex1);
            mem_block->target = bloque_actual->solution;
            mem_block->solution = -1;
            mem_block->id ++;
            mem_block->is_valid = 0;
            /*for (i = 0; i < ; i++){
                // Despierta a los procesos para una nueva ronda
            }*/
            sem_post(sem_mutex1);
            
            //Envio del bloque actual a la cola
            sem_wait(sem_mutex1);
            monitor = net_data->monitor_pid;
            sem_post(sem_mutex1);
            if(monitor){
                mq_send(queue, (char *) bloque_actual, sizeof(Block), 2);
            }  
        }
        // Perdedor
        else {
            for (i = 0; i < num_hilos; i++){
                pthread_cancel(h[i]);
                pthread_join(h[i], NULL);
                fprintf(stderr, "Hilo %d finaliza\n", i);
            }
            // Espera para votar
            sigfillset(&set);
            sigdelset(&set, SIGUSR2);
            sigdelset(&set, SIGINT);
            sigdelset(&set, SIGALRM);
            alarm(3);
            sigsuspend(&set);

            if (got_sigarlm){
                break;
            }

            // No vota
            if(got_sigint){
                sem_wait(sem_mutex2);
                net_data->total_miners --;
                sem_post(sem_mutex2);
                break;
            }
            
            // Llega sigusr2
            got_sigusr2 = 0;

            sem_wait(sem_mutex1);
            int supuesta_sol = simple_hash(mem_block->solution);
            sem_post(sem_mutex1);

            //Lee la supuesta solucion y vota
            sem_wait(sem_mutex2);
            if (supuesta_sol == bloque_actual->target){
                for (i = 0; i < MAX_MINERS && net_data->miners_pid[i] != getpid(); i++);
                net_data->voting_pool[i] = 1;
            }
            net_data->total_miners --;
            if (net_data->total_miners == 0){
                // Si falla el kill, los mineros finalizaran tras trywait o alarm
                kill (net_data->last_winner, SIGUSR2);
            }
            sem_post(sem_mutex2);

            // Espera para conocer el resultado de la votacion - validacion
            sigfillset(&set);
            sigdelset(&set, SIGUSR2);
            sigdelset(&set, SIGINT);
            sigdelset(&set, SIGALRM);
            alarm(3);
            sigsuspend(&set);

            // No copia el bloque
            if (got_sigint || got_sigarlm){
                break;
            }
            
            // Se ha recibido SIGUSR2
            got_sigusr2 = 0;

            //Copia el bloque de memoria
            sem_wait(sem_mutex1);
            bloque_actual->is_valid=mem_block->is_valid;
            bloque_actual->solution=mem_block->solution;
            sem_post(sem_mutex1);

            // Despiertan al ganador para que prepare el nuevo bloque
            sem_wait(sem_mutex2);
            if (net_data->total_miners == 0){
                kill (net_data->last_winner, SIGUSR2);
            }
            sem_post(sem_mutex2);

            //Envio del bloque actual a la cola
            sem_wait(sem_mutex1);
            monitor = net_data->monitor_pid;
            sem_post(sem_mutex1);
            if(monitor){
                mq_send(queue, (char *) bloque_actual, sizeof(Block), 1); 
            }  
        }
