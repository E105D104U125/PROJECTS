#include "pool_thread_lib.h"

void fcn() {
    printf(":)\n");
}

int main(){
    sv_pool *s = sv_pool_create(&fcn, 10);
    sv_pool_destroy(s);
}