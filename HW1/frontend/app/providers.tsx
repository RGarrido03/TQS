// app/providers.tsx
"use client";

import { NextUIProvider } from "@nextui-org/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ThemeProvider as NextThemesProvider } from "next-themes";
import { useState } from "react";

export function Providers({ children }: { children: React.ReactNode }) {
  const [queryClient] = useState(() => new QueryClient());

  return (
    <NextUIProvider>
      <NextThemesProvider attribute="class" defaultTheme="system">
          <QueryClientProvider client={queryClient}>
            {children}
          </QueryClientProvider>
      </NextThemesProvider>
    </NextUIProvider>
  );
}
